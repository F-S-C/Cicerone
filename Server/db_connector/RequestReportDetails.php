<?php

namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

class RequestReportDetails extends JsonConnector
{
    private $code;

    public function __construct(string $code = null)
    {
        $this->code = $code;
        parent::__construct();
    }

    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM report_details";
        if (isset($this->code)) {
            $query .= " WHERE report_code = ?";
        }

        if ($statement = $this->connection->prepare($query)) {
            if (isset($this->code)) {
                $statement->bind_param("i", $this->code);
            }
            if ($statement->execute()) {
                $to_return = $statement->get_result()->fetch_all(MYSQLI_ASSOC);
            } else {
                throw new mysqli_sql_exception($statement->error);
            }
        } else {
            throw new mysqli_sql_exception($this->connection->error);
        }

        return $to_return;
    }


}

$connector = new RequestLanguage($_POST['report_code']);
print $connector->get_content();