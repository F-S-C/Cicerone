<?php


namespace database_connector\controller;


use mysqli;
use mysqli_sql_exception;

/**
 * A generic connector to a database.
 * @package database_connector\controller
 */
abstract class DatabaseConnector
{
    /** @var string The database's server's IP */
    protected const DB_SERVER_NAME = "127.0.0.1";

    /** @var string The database's username. */
    protected const DB_USERNAME = "fsc";

    /** @var string The database's password. */
    protected const DB_P = "89n@W[";

    /** @var string The database's name. */
    protected const DB_NAME = "my_fsc";

    /** @var mysqli|null A reference to the database connection. */
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

    /**
     * A utility function used to reduce the code duplication.
     * It executes a query.
     * @param string $query The query to be executed (in the form of a prepared statement).
     * @param array $parameters The parameters to be bond to the query.
     * @param string $types The types of the parameters concatenated in a string.
     * @return array An associative array containing the value fetched from the database.
     * @throws mysqli_sql_exception If the execution of the query ended with an error.
     */
    protected function execute_query(string $query, array &$parameters, string $types): array
    {
        $to_return = null;

        if ($statement = $this->connection->prepare($query)) {
            if (!empty($parameters)) {
                $statement->bind_param($types, ...$parameters);
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

    /**
     * Get a result from another Database connector.
     * @param DatabaseConnector $connector The other connector.
     * @return array The result of the connection in an associative array.
     */
    protected function get_from_connector(DatabaseConnector $connector): array
    {
        // TODO: Add to class diagram.
        return json_decode($connector->get_content(), true);
    }
}