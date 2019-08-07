<?php


namespace db_connector;

use mysqli_sql_exception;

require_once("JsonConnector.php");

/**
 * Request the basic report information and the details of the report.
 * @package db_connector
 */
class RequestReportJoinReportDetails extends JsonConnector
{
    /** @var string|null The report's code. */
    private $report_code;
    /** @var string|null The report's author's username. */
    private $username;
    /** @var string|null The reported user's username. */
    private $reported_user;

    /**
     * RequestReportJoinReportDetails constructor.
     * @param string|null $report_code The report's code.
     * @param string|null $reported_user The reported user's username.
     * @param string|null $username The report's author's username.
     */
    public function __construct(string $report_code = null, string $reported_user = null, string $username = null)
    {
        $this->report_code = isset($report_code) && $report_code != "" ? $report_code : null;
        $this->username = isset($username) && $username != "" ? strtolower($username) : null;
        $this->reported_user = isset($reported_user) && $reported_user != "" ? strtolower($reported_user) : null;
        parent::__construct();
    }

    protected function fetch_all_rows(): array
    {
        $query = "SELECT * FROM report INNER JOIN report_details ON report.report_code = report_details.report_code";

        $conditions = array();
        $data = array();
        $types = "";
        if (isset($this->reported_user)) {
            array_push($conditions, "reported_user = ?");
            array_push($data, $this->reported_user);
            $types .= "s";
        }
        if (isset($this->username)) {
            array_push($conditions, "username = ?");
            array_push($data, $this->username);
            $types .= "s";
        }
        if (isset($this->report_code)) {
            array_push($conditions, "report.report_code = ?");
            array_push($data, $this->report_code);
            $types .= "s";
        }
        $query .= $this->create_SQL_WHERE_clause($conditions);


        if ($statement = $this->connection->prepare($query)) {
            if (isset($this->reported_user) || isset($this->username)) {
                $statement->bind_param($types, ...$data);
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

$connector = new RequestReportJoinReportDetails($_POST['report_code'], $_POST['reported_user'], $_POST['username']);
print $connector->get_content();