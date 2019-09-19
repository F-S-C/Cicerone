<?php


namespace database_connector\controller\request;

use database_connector\controller\JsonConnector;

require_once "/membri/fsc/database_connector/controller/JsonConnector.php";
require_once "/membri/fsc/database_connector/controller/request/RequestRegisteredUser.php";

/**
 * Request the reports to the admin.
 * @package database_connector\controller\request
 */
class RequestReport extends JsonConnector
{
    /** @var string|null The report's code. */
    private $report_code;
    /** @var string|null The report's author's username. */
    private $username;
    /** @var string|null The reported user's username. */
    private $reported_user;

    /**
     * RequestReport constructor.
     * @param int|null $report_code The report's code.
     * @param string|null $reported_user The reported user's username.
     * @param string|null $username The report's author's username.
     */
    public function __construct(int $report_code = null, string $reported_user = null, string $username = null)
    {
        $this->report_code = isset($report_code) && $report_code != "" ? $report_code : null;
        $this->username = isset($username) && $username != "" ? strtolower($username) : null;
        $this->reported_user = isset($reported_user) && $reported_user != "" ? strtolower($reported_user) : null;
        parent::__construct();
    }

    /**
     * @see JsonConnector::fetch_all_rows()
     */
    protected function fetch_all_rows(): array
    {
        $query = "SELECT report.username, report.reported_user, report_details.* FROM report INNER JOIN report_details ON report.report_code = report_details.report_code";

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
            array_push($conditions, "report_code = ?");
            array_push($data, $this->report_code);
            $types .= "i";
        }
        $query .= $this->create_SQL_WHERE_clause($conditions);


        $to_return = $this->execute_query($query, $data, $types);
        foreach ($to_return as &$row) {
            $row["username"] = $this->get_from_connector(new RequestRegisteredUser($row["username"]))[0];
            $row["reported_user"] = $this->get_from_connector(new RequestRegisteredUser($row["reported_user"]))[0];
        }

        return $to_return;
    }
}