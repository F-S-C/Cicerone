<?php


namespace database_connector\controller;


/**
 * The Database Interface.
 */
interface DatabaseInterface
{
    /**
     * Get the content fetched by the connector (or the result of a connection), if any.
     * @return string A string that contains the result of the connection.
     */
    public function get_content():string;
}