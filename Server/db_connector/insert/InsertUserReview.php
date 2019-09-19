<?php

namespace db_connector\insert;

require_once "InsertConnector.php";

/**
 * Insert a review for a user.
 * @package db_connector
 */
class InsertUserReview extends InsertConnector
{
    protected const COLUMNS = "username, reviewed_user, feedback, description";
    protected const COLUMNS_TYPE = "ssis";
    protected const TABLE_NAME = "user_review";
}