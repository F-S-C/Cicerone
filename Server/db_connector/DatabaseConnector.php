<?php


namespace db_connector;


use mysqli;

/**
 * A generic connector to a database.
 * @package db_connector
 */
abstract class DatabaseConnector
{
    /**
     * @var string The database's server's IP
     */
    protected const DB_SERVER_NAME = "127.0.0.1";

    /**
     * @var string The database's username.
     */
    protected const DB_USERNAME = "fsc";

    /**
     * @var string The database's password.
     */
    protected const DB_P = "89n@W[";

    /**
     * @var string The database's name.
     */
    protected const DB_NAME = "cicerone";

    /**
     * @var mysqli|null A reference to the database connection.
     */
    protected $connection = null;

    /**
     * DatabaseConnector constructor.
     */
    public function __construct()
    {
        $this->connection = new mysqli(self::DB_SERVER_NAME, self::DB_USERNAME, self::DB_P, self::DB_NAME);
        if ($this->connection->connect_error) {
            die("Connection failed: " . $this->connection->connect_error);
        }
    }

    /**
     * DatabaseConnector destructor.
     */
    public function __destruct()
    {
        $this->connection->close();
    }

    /**
     * Get the content fetched by the connector (or the result of a connection), if any.
     * @return string A string that contains the result of the connection.
     */
    public abstract function get_content(): string;

    /**
     * Get an SQL WHERE clause based on an array of conditions.
     * @param array $conditions The array of conditions. It must be an array of SQL valid conditions.
     * @return string The WHERE clause (including the WHERE keyword).
     */
    protected function create_SQL_WHERE_clause(array $conditions): string
    {
        $where_clause = "";
        if (count($conditions) > 0) {
            $where_clause .= " WHERE ";
            while (count($conditions) > 0) {
                $where_clause .= $conditions[0] ?? '';
                array_shift($conditions);
                if (count($conditions) > 0) {
                    $where_clause .= " AND ";
                }
            }
        }
        return $where_clause;
    }
}