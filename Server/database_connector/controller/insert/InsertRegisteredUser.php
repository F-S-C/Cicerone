<?php

namespace database_connector\controller\insert;

require_once "/home/fsc/www/database_connector/controller/insert/InsertConnector.php";
require_once "/home/fsc/www/database_connector/controller/insert/InsertDocument.php";

/**
 * Insert a new user.
 */
class InsertRegisteredUser extends InsertConnector
{
    protected const COLUMNS = "username, tax_code, name, surname, password, email, user_type, cellphone, birth_date, sex";
    protected const COLUMNS_TYPE = "ssssssisss";
    protected const TABLE_NAME = "registered_user";

    public function get_content(): string
    {
        $doc = json_decode($this->values_to_add["document"], true);
        unset($this->values_to_add["document"]);
        $to_return = parent::get_content();

        $document_connector = new InsertDocument();
        $document_connector->add_value(array(
            $this->values_to_add[0],
            $doc["document_number"],
            $doc["document_type"],
            $doc["expiry_date"]
        ));
        $document_connector->get_content();

        return $to_return;
    }
}