<?php

namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

/**
 * Request the details for a report.
 * @package db_connector
 */
class RequestReportDetails extends JsonConnector
{
    /** @var int|null The report's code. */
    private $code;

    /**
     * RequestReportDetails constructor.
     * @param int|null $code The report's code.
     */
    public function __construct(int $code = null)
    {
        $this->code = isset($code) ? $code : null;
        parent::__construct();
    }

    /**
     * @see JsonConnector::fetch_all_rows()
     */
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

$connector = new RequestReportDetails($_POST['report_code']);
print $connector->get_content();