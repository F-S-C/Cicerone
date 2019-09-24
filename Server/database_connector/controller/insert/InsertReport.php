<?php

namespace database_connector\controller\insert;

require_once "/home/fsc/www/database_connector/controller/insert/InsertConnector.php";
require_once "/home/fsc/www/database_connector/controller/insert/InsertReportDetails.php";

/**
 * Insert a report for the admin.
 */
class InsertReport extends InsertConnector
{
    protected const COLUMNS = "username, reported_user";
    protected const COLUMNS_TYPE = "ss";
    protected const TABLE_NAME = "report";

    public function get_content(): string
    {
        $for_details = array();
        foreach ($this->values_to_add as &$value) {
            array_push($for_details, array_slice($value, 2));
            $value = array_slice($value, 0, 2);
        }

        $result = json_decode(parent::get_content(), true);
        if (!$result[self::RESULT_KEY]) {
            $result["error"] = $result["error"] . " (in main)";
            die(json_encode($result));
        }

        $id = $this->connection->insert_id;

        $connector = new InsertReportDetails();
        foreach ($for_details as &$row) {
            array_unshift($row, $id);
            $connector->add_value($row);
        }

        $result = json_decode($connector->get_content(), true);
        if (!$result[self::RESULT_KEY]) {
            $prepared = $this->connection->prepare("DELETE FROM report WHERE report_code = ?");
            $prepared->bind_param("i", $id);
            $prepared->execute();
            $result["error"] = $result["error"] . " (in details)";
            die(json_encode($result));
        }

        return json_encode($result);
    }
}