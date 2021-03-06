<?php
/*
TODO:
 Alcune porzioni di codice sono ripetitive e difficili da modificare
 in  seguito. Tale problema sarà risolto in un futuro sprint in cui si effettuerà
 una riorganizzazione totale dei connettori PHP, consentendo così un maggiore
 riuso del codice tra i vari connettori e, di conseguenza, in questo
 connettore.
 */

namespace database_connector\controller\download;

require_once "/home/fsc/www/database_connector/controller/DatabaseConnector.php";
require_once "/home/fsc/www/database_connector/controller/request/UserLogin.php";
require_once "/home/fsc/www/database_connector/controller/download/utilities/TableCreator.php";

use database_connector\controller\DatabaseConnector;
use database_connector\controller\download\utilities\TableCreator;
use database_connector\controller\request\UserLogin;

/**
 * Download all the stored data regarding a user in a human-readable format (in
 * HTML format).
 */
class UserDataDownloader extends DatabaseConnector
{
    /** @var string A constant holding the custom CSS for the page. */
    private static $CUSTOM_STYLE = ".cmss-10{font-family:sans-serif}.cmss-10{font-family:sans-serif}.cmss-10{font-family:sans-serif}.cmss-10{font-family:sans-serif}.cmss-10{font-family:sans-serif}.cmss-10{font-family:sans-serif}.cmss-10{font-family:sans-serif}.cmss-17{font-size:170%;font-family:sans-serif}.cmss-17{font-family:sans-serif}.cmss-17{font-family:sans-serif}.cmss-17{font-family:sans-serif}.cmss-17{font-family:sans-serif}.cmss-17{font-family:sans-serif}.cmss-17{font-family:sans-serif}.cmss-12{font-size:120%;font-family:sans-serif}.cmss-12{font-family:sans-serif}.cmss-12{font-family:sans-serif}.cmss-12{font-family:sans-serif}.cmss-12{font-family:sans-serif}.cmss-12{font-family:sans-serif}.cmss-12{font-family:sans-serif}.cmtt-10{font-family:monospace}.cmss-9{font-size:90%;font-family:sans-serif}.cmss-9{font-family:sans-serif}.cmss-9{font-family:sans-serif}.cmss-9{font-family:sans-serif}.cmss-9{font-family:sans-serif}.cmss-9{font-family:sans-serif}.cmss-9{font-family:sans-serif}.cmssbx-10x-x-90{font-size:90%;font-family:sans-serif;font-weight:700}.cmssbx-10x-x-90{font-family:sans-serif;font-weight:700}.cmssbx-10x-x-90{font-family:sans-serif;font-weight:700}.cmssbx-10x-x-90{font-family:sans-serif;font-weight:700}.cmssbx-10x-x-90{font-family:sans-serif;font-weight:700}.cmssbx-10x-x-90{font-family:sans-serif;font-weight:700}.cmssbx-10x-x-90{font-family:sans-serif;font-weight:700}.cmssbx-10{font-family:sans-serif;font-weight:700}.cmssbx-10{font-family:sans-serif;font-weight:700}.cmssbx-10{font-family:sans-serif;font-weight:700}.cmssbx-10{font-family:sans-serif;font-weight:700}.cmssbx-10{font-family:sans-serif;font-weight:700}.cmssbx-10{font-family:sans-serif;font-weight:700}.cmssbx-10{font-family:sans-serif;font-weight:700}.cmssi-10{font-family:sans-serif;font-style:oblique}.cmssi-10{font-family:sans-serif;font-style:oblique}.cmssi-10{font-family:sans-serif;font-style:oblique}.cmssi-10{font-family:sans-serif;font-style:oblique}.cmssi-10{font-family:sans-serif;font-style:oblique}.cmssi-10{font-family:sans-serif;font-style:oblique}.cmssi-10{font-family:sans-serif;font-style:oblique}p.noindent{text-indent:0}td p.noindent{text-indent:0;margin-top:0}p.nopar{text-indent:0}p.indent{text-indent:1.5em}@media print{div.crosslinks{visibility:hidden}}@media screen and (max-width:767px){body{margin:1em; width: 100%;}}a img{border-top:0;border-left:0;border-right:0}center{margin-top:1em;margin-bottom:1em}td center{margin-top:0;margin-bottom:0}.Canvas{position:relative}img.math{vertical-align:middle}li p.indent{text-indent:0}li p:first-child{margin-top:0}li div:last-child,li p:last-child{margin-bottom:.5em}li p~ol:last-child,li p~ul:last-child{margin-bottom:.5em}.enumerate1{list-style-type:decimal}.enumerate2{list-style-type:lower-alpha}.enumerate3{list-style-type:lower-roman}.enumerate4{list-style-type:upper-alpha}div.newtheorem{margin-bottom:2em;margin-top:2em}.obeylines-h,.obeylines-v{white-space:nowrap}div.obeylines-v p{margin-top:0;margin-bottom:0}.overline{text-decoration:overline}.overline img{border-top:1px solid #000}td.displaylines{text-align:center;white-space:nowrap}.centerline{text-align:center}.rightline{text-align:right}div.verbatim{font-family:monospace;white-space:nowrap;text-align:left;clear:both}.fbox{padding-left:3pt;padding-right:3pt;text-indent:0;border:solid #000 .4pt}div.fbox{display:table}div.center div.fbox{text-align:center;clear:both;padding-left:3pt;padding-right:3pt;text-indent:0;border:solid #000 .4pt}div.minipage{width:100%}div.center,div.center div.center{text-align:center;margin-left:1em;margin-right:1em}div.center div{text-align:left}div.flushright,div.flushright div.flushright{text-align:right}div.flushright div{text-align:left}div.flushleft{text-align:left}.underline{text-decoration:underline}.underline img{border-bottom:1px solid #000;margin-bottom:1pt}.framebox-c,.framebox-l,.framebox-r{padding-left:3pt;padding-right:3pt;text-indent:0;border:solid #000 .4pt}.framebox-c{text-align:center}.framebox-l{text-align:left}.framebox-r{text-align:right}span.thank-mark{vertical-align:super}span.footnote-mark a sup.textsuperscript,span.footnote-mark sup.textsuperscript{font-size:80%}div.center div.tabular,div.tabular{text-align:center;margin-top:.5em;margin-bottom:.5em}table.tabular td p{margin-top:0}table.tabular{margin-left:auto;margin-right:auto}td p:first-child{margin-top:0}td p:last-child{margin-bottom:0}div.td00{margin-left:0;margin-right:0}div.td01{margin-left:0;margin-right:5pt}div.td10{margin-left:5pt;margin-right:0}div.td11{margin-left:5pt;margin-right:5pt}table[rules]{border-left:solid #000 .4pt;border-right:solid #000 .4pt}td.td00{padding-left:0;padding-right:0}td.td01{padding-left:0;padding-right:5pt}td.td10{padding-left:5pt;padding-right:0}td.td11{padding-left:5pt;padding-right:5pt}table[rules]{border-left:solid #000 .4pt;border-right:solid #000 .4pt}.cline hr,.hline hr{height:1px;margin:0}.tabbing-right{text-align:right}span.TEX{letter-spacing:-.125em}span.TEX span.E{position:relative;top:.5ex;left:-.0417em}a span.TEX span.E{text-decoration:none}span.LATEX span.A{position:relative;top:-.5ex;left:-.4em;font-size:85%}span.LATEX span.TEX{position:relative;left:-.4em}div.figure,div.float{margin-left:auto;margin-right:auto}div.float img{text-align:center}div.figure img{text-align:center}.marginpar{width:20%;float:right;text-align:left;margin-left:auto;margin-top:.5em;font-size:85%;text-decoration:underline}.marginpar p{margin-top:.4em;margin-bottom:.4em}table.equation{width:100%}.equation td{text-align:center}td.equation{margin-top:1em;margin-bottom:1em}td.equation-label{width:5%;text-align:center}td.eqnarray4{width:5%;white-space:normal}td.eqnarray2{width:5%}table.eqnarray,table.eqnarray-star{width:100%}div.eqnarray{text-align:center}div.array{text-align:center}div.pmatrix{text-align:center}table.pmatrix{width:100%}span.pmatrix img{vertical-align:middle}div.pmatrix{text-align:center}table.pmatrix{width:100%}span.bar-css{text-decoration:overline}img.cdots{vertical-align:middle}.likepartToc,.likepartToc a,.partToc,.partToc a{line-height:200%;font-weight:700;font-size:110%}.index-item,.index-subitem,.index-subsubitem{display:block}div.caption{text-indent:-2em;margin-left:3em;margin-right:1em;text-align:left}div.caption span.id{font-weight:700;white-space:nowrap}h1.partHead{text-align:center}p.bibitem{text-indent:-2em;margin-left:2em;margin-top:.6em;margin-bottom:.6em}p.bibitem-p{text-indent:0;margin-left:2em;margin-top:.6em;margin-bottom:.6em}.likeparagraphHead,.paragraphHead{margin-top:2em;font-weight:700}.likesubparagraphHead,.subparagraphHead{font-weight:700}.quote{margin-bottom:.25em;margin-top:.25em;margin-left:1em;margin-right:1em;text-align:justify}.verse{white-space:nowrap;margin-left:2em}div.maketitle{text-align:center}h2.titleHead{text-align:center}div.maketitle{margin-bottom:2em}div.author,div.date{text-align:center}div.thanks{text-align:left;margin-left:10%;font-size:85%;font-style:italic}div.author{white-space:nowrap}.quotation{margin-bottom:.25em;margin-top:.25em;margin-left:1em}.abstract p{margin-left:5%;margin-right:5%}div.abstract{width:100%}.rotatebox{display:inline-block}.figure img.graphics{margin-left:10%}#TBL-2 colgroup{border-left:1px solid #000;border-right:1px solid #000}#TBL-2{border-collapse:collapse}#TBL-2 colgroup{border-left:1px solid #000;border-right:1px solid #000}#TBL-2{border-collapse:collapse}body{font-family:'Helvetica Neue',Arial,Freesans,clean,sans-serif;padding:1em;font-size:14px;font-weight:300;line-height:20px;margin:10em;max-width:42em;background:#fefefe;color:#333;scroll-behavior:smooth}h1,h2,h3,h4,h5,h6{font-weight:700;margin:2em 0 15px 0}h1{color:#000;font-size:2em}h2{font-size:2em}h3{font-size:1.6em}h4{font-size:1.3em}h5{font-size:1em}h6{color:#777;background-color:inherit;font-size:1em}hr{height:.2em;border:0;color:#ccc;background-color:#ccc}blockquote,dl,li,ol,p,pre,table,ul{margin:15px 0}code,pre{border-radius:3px;background-color:#f8f8f8;color:inherit}code{border:1px solid #eaeaea;margin:0 2px;padding:0 5px}pre{border:1px solid #ccc;line-height:1.25em;overflow:auto;padding:6px 10px}pre>code{border:0;margin:0;padding:0}a,a:visited{color:#4183c4;background-color:inherit;text-decoration:none}img{max-width:100%}table{width:80%!important;border-collapse:collapse;border-spacing:0;border-color:#ccc;margin:15px auto}table td{padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#fff}table th{font-weight:400;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#ccc;color:#333;background-color:#f0f0f0}ol{counter-reset:item}li{display:block}li:before{content:counters(item, '.') ' ';counter-increment:item}body{counter-reset:h2counter}h1{counter-reset:h2counter}h2{counter-increment:h2counter;counter-reset:h3counter}h3{counter-increment:h3counter;counter-reset:h4counter}h2:before{content:counter(h2counter) '.\\0000a0\\0000a0'}h3:before{content:counter(h2counter) '.' counter(h3counter) '.\\0000a0\\0000a0'}h2.nocount:before{content:none;counter-increment:none}h4{counter-increment:h4counter}h4:before{content:counter(h2counter) '.' counter(h3counter) '.' counter(h4counter) '.\\0000a0\\0000a0'}h3.nocount:before{content:none;counter-increment:none}";
    /** @var string The username of the user whose data will be downloaded. */
    private $username;
    /** @var string A string that will be printed if there are no rows in a table. */
    private const NO_ROWS = "<p>No rows</p>";
    /** @var string A string that will be printed to hide useless details. */
    private const OMISSIS = "<i>Omissis</i>";
    /** @var string The user's password */
    private $password;

    /**
     * UserDataDownloader constructor.
     *
     * @param string $username The username of the user whose data will be downloaded.
     * @param string $password The user's password.
     */
    public function __construct(string $username, string $password)
    {
        parent::__construct();
        $this->username = isset($username) && $username != "" ? strtolower($username) : null;
        $this->password = isset($password) && $password != "" ? $password : null;
        if ($this->username == null || $this->password == null) {
            die("No credentials given");
        }

        $logged = $this->get_from_connector(new UserLogin($username, $password))["result"];
        if (!$logged) {
            exit('Wrong login data');
        }

        self::$CUSTOM_STYLE = file_get_contents('/home/fsc/www/database_connector/controller/download/style.min.css');
    }

    /**
     * UserDataDownloader destructor.
     */
    public function __destruct()
    {
        $this->connection->close();
    }

    /**
     * Get all the user's information in a readable format.
     *
     * @return string The user's information.
     */
    public function get_content(): string
    {
        $to_return = $this->get_header();

        $to_return .= $this->get_basic_informations();

        $to_return .= "<h2 class='sectionHead' id='reports'>Reports</h2>";
        $to_return .= $this->get_reports(true) . $this->get_reports(false);

        $to_return .= "<h2 class='sectionHead' id='reviews'>Reviews</h2>";
        $to_return .= "<h3 class='subsectionHead' id='written-reviews'>Written Reviews</h3>";
        $to_return .= $this->get_itineraries_reviews(true) . $this->get_user_reviews(true);
        $to_return .= "<h3 class='subsectionHead' id='received-reviews'>Received Reviews</h3>";
        $to_return .= $this->get_itineraries_reviews(false) . $this->get_user_reviews(false);

        $to_return .= "<h2 class='sectionHead' id='itineraries'>Itineraries</h2>";
        $to_return .= $this->get_itineraries() . $this->get_reservations() . $this->get_wishlist();

        return $to_return . $this->get_footer();
    }

    /**
     * Get all the reviews to itineraries (written or received).
     *
     * @param bool $written_by_logged_user Should the reviews have been written by the user?
     * @return string All the reviews in a readable format.
     */
    private function get_itineraries_reviews(bool $written_by_logged_user): string
    {
        $id = ($written_by_logged_user ? "Sent" : "Received");
        $to_return = "<h4 class='subsubsectionHead' id='" . strtolower($id) . "-itineraries-reviews'>" . $id . " Itineraries Reviews</h4>";

        $statement = $this->connection->prepare("SELECT ir.username, ir.reviewed_itinerary, ir.feedback, ir.description,i.username FROM itinerary_review ir, itinerary i WHERE ir.reviewed_itinerary = i.itinerary_code AND " . ($written_by_logged_user ? "ir" : "i") . ".username = ?");
        if ($statement) {
            $statement->bind_param("s", $this->username);
            $statement->execute();
            $statement->bind_result($reviewer, $reviewed_itinerary, $feedback, $description, $itinerary_owner);
            $number_of_rows = 0;
            $table_creator = new TableCreator(5);
            $table_creator->set_header(array("Reviewer", "Reviewed", "Owner", "Feedback", "Description"));
            while ($statement->fetch()) {
                $number_of_rows++;
                $table_creator->add_row(array($reviewer, $reviewed_itinerary, $itinerary_owner, $feedback, $description));
            }
            if ($number_of_rows == 0) {
                $to_return .= self::NO_ROWS;
            } else {
                $to_return .= "<div class='table-responsive text-nowrap'>" . $table_creator->get_table() . "</div>";
            }
            $statement->close();
        }

        return $to_return;
    }

    /**
     * Get all the itineraries in the wishlist.
     *
     * @return string The entire wishlist in a readable format.
     */
    private function get_wishlist(): string
    {
        $to_return = "<h3 class='subsectionHead' id='wishlist-itineraries'>Itineraries in Wishlist</h3>";

        if ($statement = $this->connection->prepare("SELECT * FROM wishlist w, itinerary i WHERE w.itinerary_in_wishlist = i.itinerary_code AND w.username = ?")) {
            $statement->bind_param("s", $this->username);
            $statement->execute();
            $result = $statement->get_result();
            if ($result->num_rows == 0) {
                return $to_return . self::NO_ROWS;
            }
            $table_creator = new TableCreator(15);
            $table_creator->set_super_header(array("Itinerary in Wishlist"), array(15));
            $table_creator->set_header(array(
                "Itinerary Code",
                "Owner",
                "Title",
                "Description",
                "Beginning Date",
                "Ending Date",
                "End of Reservations Date",
                "Max. Participants",
                "Min. Participants",
                "Location",
                "Repetitions per Day",
                "Duration",
                "Full price",
                "Reduced Price",
                "Languages"
            ));

            while ($row = $result->fetch_assoc()) {
                $table_creator->add_row(array(
                    $row['itinerary_code'],
                    $row['itinerary.username'],
                    $row['title'],
                    self::OMISSIS,
                    $row['beginning_date'],
                    $row['ending_date'],
                    $row['end_reservations_date'],
                    $row['maximum_partecipants_number'],
                    $row['minimum_partecipants_number'],
                    $row['location'],
                    $row['repetitions_per_day'],
                    $row['duration'],
                    $row['full_price'],
                    $row['reduced_price'],
                    $this->get_languages($row['itinerary_code'], false)
                ));
            }
            $to_return .= "<div class='table-responsive text-nowrap'>" . $table_creator->get_table() . "</div>";
            $statement->close();
        }

        return $to_return;
    }

    /**
     * Get all the reservations.
     *
     * @return string The reservations in a readable format.
     */
    private function get_reservations(): string
    {
        $to_return = "<h3 class='subsectionHead' id='reserved-itineraries'>Reserved Itineraries</h3>";

        if ($statement = $this->connection->prepare("SELECT * FROM reservation r, itinerary i WHERE r.booked_itinerary = i.itinerary_code AND r.username = ?")) {
            $statement->bind_param("s", $this->username);
            $statement->execute();
            $result = $statement->get_result();
            if ($result->num_rows == 0) {
                return $to_return . self::NO_ROWS;
            }
            $table_creator = new TableCreator(21);
            $table_creator->set_super_header(array("Reservations", "Itinerary"), array(6, 15));
            $table_creator->set_header(array(
                "Number of Children",
                "Number of Audults",
                "Total",
                "Requested Date",
                "Forwading Date",
                "Confim Date",
                "Itinerary Code",
                "Owner",
                "Title",
                "Description",
                "Beginning Date",
                "Ending Date",
                "End of Reservations Date",
                "Max. Participants",
                "Min. Participants",
                "Location",
                "Repetitions per Day",
                "Duration",
                "Full price",
                "Reduced Price",
                "Languages"
            ));
            while ($row = $result->fetch_assoc()) {
                $table_creator->add_row(array(
                    $row['number_of_children'],
                    $row['number_of_adults'],
                    $row['total'],
                    $row['requested_date'],
                    $row['forwading_date'],
                    $row['confirm_date'],
                    $row['itinerary_code'],
                    $row['username'],
                    $row['title'],
                    self::OMISSIS,
                    $row['beginning_date'],
                    $row['ending_date'],
                    $row['end_reservations_date'],
                    $row['maximum_partecipants_number'],
                    $row['minimum_partecipants_number'],
                    $row['location'],
                    $row['repetitions_per_day'],
                    $row['duration'],
                    $row['full_price'],
                    $row['reduced_price'],
                    $this->get_languages($row['itinerary_code'], false)
                ));
            }
            $to_return .= "<div class='table-responsive text-nowrap'>" . $table_creator->get_table() . "</div>";
            $statement->close();
        }

        return $to_return;
    }

    /**
     * Get all the itineraries created by the user.
     *
     * @return string The itineraries in a readable format.
     */
    private function get_itineraries(): string
    {
        $to_return = "<h3 class='subsectionHead' id='created-itineraries'>Created Itineraries</h3>";

        if ($statement = $this->connection->prepare("SELECT * FROM itinerary WHERE username = ?")) {
            $statement->bind_param("s", $this->username);
            $statement->execute();
            $result = $statement->get_result();
            if ($result->num_rows == 0) {
                return $to_return . self::NO_ROWS;
            }
            $table_creator = new TableCreator(15);
            $table_creator->set_super_header(array("Itinerary"), array(15));
            $table_creator->set_header(array(
                "Code",
                "Owner",
                "Title",
                "Description",
                "Beginning Date",
                "Ending Date",
                "End of Reservations Date",
                "Max. Participants",
                "Min. Participants",
                "Location",
                "Repetitions per Day",
                "Duration",
                "Full price",
                "Reduced Price",
                "Languages"
            ));

            while ($row = $result->fetch_assoc()) {
                $table_creator->add_row(array(
                    $row['itinerary_code'],
                    $row['username'],
                    $row['title'],
                    self::OMISSIS,
                    $row['beginning_date'],
                    $row['ending_date'],
                    $row['end_reservations_date'],
                    $row['maximum_partecipants_number'],
                    $row['minimum_partecipants_number'],
                    $row['location'],
                    $row['repetitions_per_day'],
                    $row['duration'],
                    $row['full_price'],
                    $row['reduced_price'],
                    $this->get_languages($row['itinerary_code'], false)
                ));
            }

            $to_return .= "<div class='table-responsive text-nowrap'>" . $table_creator->get_table() . "</div>";
            $statement->close();
        }

        return $to_return;
    }

    /**
     * Get the header for the HTML file.
     *
     * @return string The header of the HTML file.
     */
    private function get_header(): string
    {
        return "<!DOCTYPE html>
        <html lang='en'>
        
        <head>
            <meta charset='UTF-8'>
            <meta name='viewport' content='width=device-width, initial-scale=1.0'>
            <meta http-equiv='X-UA-Compatible' content='ie=edge'>
            <title>$this->username's Data</title>
            <style>" .
            self::$CUSTOM_STYLE .
            "</style>
        </head>
        
        <body>
            <div class='container'>
                <header class='text-center'>
                    <h1>User Data: @$this->username</h1>
                    <div><span>Generated by Cicerone (&copy; FSC)</span></div>
                    <div><span>Generated at " . date('m/d/Y h:i:s a', time()) . "</span></div>
                </header>
                <hr />
                <div class='tableofcontents'>
                    <h3 class='likesectionHead nocount'><a id='x1-1000'></a>Contents</h3>
                    <ol>
                        <li><a href='#basic'>Basic information</a>
                            <ol>
                                <li><a href='#documents'>Documents</a></li>
                            </ol>
                        </li>
                        <li><a href='#reports'>Reports</a>
                            <ol>
                                <li><a href='#sent-reports'>Sent Reports</a></li>
                                <li><a href='#received-reports'>Received Reports</a></li>
                            </ol>
                        </li>
                        <li><a href='#reviews'>Reviews</a>
                            <ol>
                                <li><a href='#written-reviews'>Written Reviews</a>
                                    <ol>
                                        <li><a href='#sent-itineraries-reviews'>Sent Itineraries Reviews</a></li>
                                        <li><a href='#sent-user-reviews'>Sent User Reviews</a></li>
                                    </ol>
                                </li>
                                <li><a href='#received-reviews'>Received Reviews</a>
                                    <ol>
                                        <li><a href='#received-itineraries-reviews'>Received Itineraries Reviews</a></li>
                                        <li><a href='#received-user-reviews'>Received User Reviews</a></li>
                                    </ol>
                                </li>
                            </ol>
                        </li>
                        <li><a href='#itineraries'>Itineraries</a>
                            <ol>
                                <li><a href='#created-itineraries'>Created Itineraries</a></li>
                                <li><a href='#reserved-itineraries'>Reserved Itineraries</a></li>
                                <li><a href='#wishlist-itineraries'>Itineraries in Wishlist</a></li>
                            </ol>
                        </li>
                    </ol>
                </div>";
    }

    /**
     * Get all the languages associated with a user or an itinerary.
     *
     * @param string $id The id of the object to which the languages are associated.
     * @param bool $user_languages Should the languages be searched for a user?
     * @return string The languages in a readable format.
     */
    private function get_languages(string $id, bool $user_languages = true): string
    {
        $languages = "";
        if ($statement = $this->connection->prepare("SELECT ul.language_code, l.language_name FROM " . ($user_languages ? "user_language" : "itinerary_language") . " ul, language l WHERE l.language_code = ul.language_code AND " . ($user_languages ? "username" : "itinerary_code") . " = ?")) {
            $statement->bind_param("s", $id);
            $statement->execute();
            $statement->bind_result($code, $name);
            while ($statement->fetch()) {
                $languages .= ", " . $name . "(" . $code . ")";
            }
        }
        return ($languages != "") ? substr($languages, 2) : "No languages given";
    }

    /**
     * Get all the basic data associated to the user.
     *
     * @return string The user's basic information in a readable format.
     */
    private function get_basic_informations(): string
    {
        $to_return = "";
        if ($statement = $this->connection->prepare("SELECT tax_code, name, surname, email, user_type, cellphone, birth_date, sex FROM registered_user WHERE username = ?")) {
            $statement->bind_param("s", $this->username);
            $statement->execute();
            $statement->bind_result($tax_code, $name, $surname, $email, $user_type, $cellphone, $birth_date, $sex);
            $statement->fetch();
            $to_return = "<h2 class='sectionHead' id='basic'>Basic information</h2>";
            $to_return .= "<table class='table table-striped table-bordered'>
            <tr>
                <th scope='row'>Username</th>
                <td>$this->username</td>
            </tr>
            <tr>
                <th scope='row'>Tax Code</th>
                <td>$tax_code</td>
            </tr>
            <tr>
                <th scope='row'>Name</th>
                <td>$name</td>
            </tr>
            <tr>
                <th scope='row'>Surname</th>
                <td>$surname</td>
            </tr>
            <tr>
                <th scope='row'>e-Mail</th>
                <td>$email</td>
            </tr>
            <tr>
                <th scope='row'>User Type</th>
                <td>" . ($user_type == 1 ? "Cicerone" : "Globetrotter") . "</td>
            </tr>
            <tr>
                <th scope='row'>Cellphone</th>
                <td>$cellphone</td>
            </tr>
            <tr>
                <th scope='row'>Birth Date</th>
                <td>$birth_date</td>
            </tr>
            <tr>
                <th scope='row'>Sex</th>
                <td>$sex</td>
            </tr>
            <tr>
                <th scope='row'>Languages</th>
                <td>" . $this->get_languages($this->username, true) . "</td>
            </tr>
        </table>";
            $statement->close();
        }

        $to_return .= "<h3 class='subsectionHead' id='documents'>Document</h3>";
        if ($statement = $this->connection->prepare("SELECT * FROM document WHERE username = ?")) {
            $statement->bind_param("s", $this->username);
            $statement->execute();
            $arr = $statement->get_result()->fetch_all(MYSQLI_ASSOC);
            if (!$arr) {
                $to_return .= '<p>No rows</p>';
            }
            $to_return .= "<dl>";
            foreach ($arr as $value) {
                foreach ($value as $column => $data) {
                    $to_return .= "<dt>" . $column . "</dt><dd>" . $data . "</dd>";
                }
            }
            $to_return .= "</dl>";
            $statement->close();
        }

        return $to_return;
    }

    /**
     * Get all the reports written or received by the logged user.
     *
     * @param bool $done_by_logged_user Should the reports be written by the logged user?
     * @return string The reports in a readable format.
     */
    private function get_reports(bool $done_by_logged_user): string
    {
        $id = $done_by_logged_user ? "Sent" : "Received";
        $to_return = "<h3 class='subsectionHead' id='" . strtolower($id) . "-reports'>" . $id . " Reports</h3>";

        $statement = $this->connection->prepare("SELECT r.report_code, r.username, r.reported_user, rd.report_body, rd.state, rd.object FROM report r, report_details rd WHERE r.report_code = rd.report_code AND " . ($done_by_logged_user ? "username = ?" : "reported_user = ?"));
        if ($statement) {
            $statement->bind_param("s", $this->username);
            $statement->execute();
            $statement->bind_result($code, $reporter, $reported, $body, $state, $object);
            $table_creator = new TableCreator(6);
            $table_creator->set_header(array("Report Code", "State", "Reporter", "Reported User", "Object", "Body"));
            $number_of_rows = 0;
            while ($statement->fetch()) {
                if (!$done_by_logged_user) {
                    $reporter = self::OMISSIS;
                }
                $number_of_rows++;
                $table_creator->add_row(array($code, $state, $reporter, $reported, $object, $body));
            }
            if ($number_of_rows == 0) {
                $to_return .= self::NO_ROWS;
            } else {
                $to_return .= "<div class='table-responsive text-nowrap'>" . $table_creator->get_table() . "</div>";
            }
            $statement->close();
        }

        return $to_return;
    }

    /**
     * Get all the reviews written or received by the logged user.
     *
     * @param bool $done_by_logged_user Should the reviews have been written by the logged user?
     * @return string the reviews in a readable format.
     */
    private function get_user_reviews(bool $done_by_logged_user): string
    {
        $to_return = "<h4 class='subsubsectionHead' id='" . ($done_by_logged_user ? "sent" : "received") . "-user-reviews'>" . ($done_by_logged_user ? "Sent" : "Received") . " User Reviews</h4>";

        $statement = $this->connection->prepare("SELECT username, reviewed_user, feedback, description FROM user_review WHERE " . ($done_by_logged_user ? "username = ?" : "reviewed_user = ?"));
        if ($statement) {
            $statement->bind_param("s", $this->username);
            $statement->execute();
            $statement->bind_result($reviewer, $reviewed, $feedback, $description);
            $number_of_rows = 0;
            while ($statement->fetch()) {
                $number_of_rows++;
                $to_return .= "<table class='table table-striped table-bordered'>
                <tr>
                    <th scope='row'>Reviewer</th>
                    <td>$reviewer</td>
                    <th scope='row'>Reviewed</th>
                    <td>$reviewed</td>
                </tr>
                <tr>
                    <th scope='row'>Feedback</th>
                    <td colspan='3'>$feedback</td>
                </tr>
                <tr>
                    <th scope='col' colspan='4'>Description</th>
                </tr>
                <tr>
                    <td colspan='4'>$description</td>
                </tr>
            </table>";
            }
            if ($number_of_rows == 0) {
                $to_return .= self::NO_ROWS;
            }
            $statement->close();
        }

        return $to_return;
    }

    /**
     * Get the HTML file's footer.
     *
     * @return string The HTML footer.
     */
    private function get_footer(): string
    {
        return "<footer class=\"page-footer font-small pt-4\">
            <div class=\"footer-copyright text-center py-3\">
                This file was automatically generated by Cicerone. &copy; 2019, FSC.</a>
            </div>
        </footer>
        </div>
        </body>
        </html>";
    }
}