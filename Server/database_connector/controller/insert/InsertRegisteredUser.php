<?php

namespace database_connector\controller\insert;

use database_connector\controller\delete\DeleteRegisteredUser;

require_once "/home/fsc/www/database_connector/controller/insert/InsertConnector.php";
require_once "/home/fsc/www/database_connector/controller/insert/InsertDocument.php";
require_once "/home/fsc/www/database_connector/controller/insert/InsertUserLanguage.php";
require_once "/home/fsc/www/database_connector/controller/delete/DeleteRegisteredUser.php";

/**
 * Insert a new user.
 */
class InsertRegisteredUser extends InsertConnector
{
    protected const COLUMNS = "username, tax_code, name, surname, password, email, user_type, cellphone, birth_date, sex";
    protected const COLUMNS_TYPE = "ssssssisss";
    protected const TABLE_NAME = "registered_user";

    /**
     * @see InsertConnector::get_content()
     */
    public function get_content(): string
    {
        $doc = array();
        $languages = array();
        foreach ($this->values_to_add as &$row) {
            $doc = json_decode($row["document"], true);
            unset($row["document"]);
            if (isset($row["languages"])) {
                array_push($languages, ...json_decode($row["languages"], true));
                unset($row["languages"]);
            }
        }
        $to_return = parent::get_content();

        if (json_decode($to_return, true)[self::RESULT_KEY]) {
            // Insert Document
            $document_connector = new InsertDocument();
            $document_connector->add_value(array(
                $this->values_to_add[0][0],
                $doc["document_number"],
                $doc["document_type"],
                $doc["expiry_date"]
            ));
            $to_return = $document_connector->get_content();

            // Insert Languages
            if (json_decode($to_return, true)[self::RESULT_KEY]) {
                if (!empty($languages)) {
                    $language_connector = new InsertUserLanguage();
                    foreach ($languages as $language) {
                        $language_connector->add_value(array($this->values_to_add[0][0], $language["language_code"]));
                    }
                    $to_return = $language_connector->get_content();
                }
            }
            if (!json_decode($to_return, true)[self::RESULT_KEY]) {
                $this->get_from_connector(new DeleteRegisteredUser($this->values_to_add[0][0]));
            }
        }

        return $to_return;
    }
}