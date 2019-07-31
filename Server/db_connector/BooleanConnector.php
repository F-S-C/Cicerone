<?php


namespace db_connector;

require_once("DatabaseConnector.php");

abstract class BooleanConnector extends DatabaseConnector
{
    protected const RESULT_KEY = "result";
    protected const MESSAGE_KEY = "message";
    protected const ERROR_KEY = "error";

    protected static function get_true(string $message = null): array
    {
        return ($message != null) ? array(self::RESULT_KEY => true, self::MESSAGE_KEY => $message) : array(self::RESULT_KEY => true);
    }

    protected static function get_false(string $error = null) : array {
        return ($error != null) ? array(self::RESULT_KEY => false, self::ERROR_KEY => $error) : array(self::RESULT_KEY => false);
    }
}