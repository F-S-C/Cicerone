<?php


namespace database_connector\controller;

require_once "/home/fsc/www/database_connector/controller/DatabaseConnector.php";

/**
 * A generic connector to a database that returns a boolean value (true or false) and a message.
 * The answer is provided in an associative array format.
 */
abstract class BooleanConnector extends DatabaseConnector
{
    /**
     * The key for the boolean result.
     */
    protected const RESULT_KEY = "result";
    /**
     * The key for the message (available if the result is true).
     */
    protected const MESSAGE_KEY = "message";
    /**
     * The key for the error (available if the result is false).
     */
    protected const ERROR_KEY = "error";

    /**
     * Get the "true" answer.
     * @param string|array|null $message The message. Ignored if null.
     * @return array The answer. It is an associative array that has <pre>"result" => true</pre> and (if available)
     * <pre>"message" => "custom message"</pre>.
     */
    protected static function get_true($message = null): array
    {
        return ($message != null) ? array(self::RESULT_KEY => true, self::MESSAGE_KEY => $message) : array(self::RESULT_KEY => true);
    }

    /**
     * Get the "false" answer.
     * @param string|null $error The error message. Ignored if null.
     * @return array The answer. It is an associative array that has <pre>"result" => false</pre> and (if available)
     * <pre>"error" => "custom message"</pre>.
     */
    protected static function get_false(string $error = null): array
    {
        return ($error != null) ? array(self::RESULT_KEY => false, self::ERROR_KEY => $error) : array(self::RESULT_KEY => false);
    }
}