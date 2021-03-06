<?php

namespace database_connector\controller\insert;

require_once "/home/fsc/www/database_connector/controller/insert/InsertConnector.php";

/**
 * Insert a review for a user.
 */
class InsertUserReview extends InsertConnector
{
    protected const COLUMNS = "username, reviewed_user, feedback, description";
    protected const COLUMNS_TYPE = "ssis";
    protected const TABLE_NAME = "user_review";
}