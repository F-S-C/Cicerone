<?php

namespace notifications;

/**
 * A model class that represents a push notification.
 */
class Push
{
    /** @var string The title of the push notification. */
    private $title;
    /** @var string The message of the push notification. */
    private $message;
    /** @var string The URL of an image for the notification. */
    private $image = null;
    /** @var array The push message payload. */
    private $data;
    /** @var bool A flag indicating whether or not to show the push notification. */
    private $is_background = false;

    //<editor-fold desc="Notification types">
    /**
     * @var string A type of notification. It represents a notification sent to a cicerone to notify a new reservation
     *     to one of his itineraries.
     */
    public const TO_CICERONE_NEW_RESERVATION = "to_cicerone_new_reservation";
    /**
     * @var string A type of notification. It represents a notification sent to a cicerone to notify that a reservation
     *     to one of his itineraries has been deleted.
     */
    public const TO_CICERONE_REMOVED_RESERVATION = "to_cicerone_removed_reservation";
    /**
     * @var string A type of notification. It represents a notification sent to a globetrotter to notify that a
     *     reservation he has requested has been confirmed.
     */
    public const TO_GLOBETROTTER_CONFIRMED_RESERVATION = "to_globetrotter_confirmed_reservation";
    /**
     * @var string A type of notification. It represents a notification sent to a globetrotter to notify that a
     *     reservation he has requested has been refused.
     */
    public const TO_GLOBETROTTER_REFUSED_RESERVATION = "to_globetrotter_refused_reservation";
    /**
     * @var string A type of notification. It represents a notification sent to a globetrotter to notify that an
     *     itinerary for which he has a reservation or reservation request has been deleted.
     */
    public const TO_GLOBETROTTER_DELETED_ITINERARY = "to_globetrotter_deleted_itinerary";
    /**
     * @var string A type of notification. It represents a notification sent to a globetrotter to notify that an
     *     itinerary for which he has a reservation or reservation request has been updated.
     */
    public const TO_GLOBETROTTER_UPDATED_ITINERARY = "to_globetrotter_updated_itinerary";
    //</editor-fold>

    /**
     * Push constructor.
     */
    function __construct()
    {
        // Do nothing
    }

    /**
     * Set the title of the notification.
     *
     * @param string $title The title.
     * @return Push The updated Push object.
     */
    public function set_title(string $title): Push
    {
        $this->title = $title;
        return $this;
    }

    /**
     * Set the message of the notification.
     *
     * @param string $message The message.
     * @return Push The updated Push object.
     */
    public function set_message(string $message): Push
    {
        $this->message = $message;
        return $this;
    }

    /**
     * Set the image's URL.
     *
     * @param string $imageUrl The image's URL.
     * @return Push The updated Push object.
     */
    public function set_image(string $imageUrl): Push
    {
        $this->image = $imageUrl;
        return $this;
    }

    /**
     * Set the notification payload. The payload could be used to handle localization.
     *
     * @param array $data The payload.
     * @return Push The updated Push object.
     */
    public function set_payload(array $data): Push
    {
        $this->data = $data;
        return $this;
    }

    /**
     * Set whether or not the notification should be shown.
     *
     * @param bool $is_background The boolean value. If false, the notification will be shown.
     * @return Push The updated Push object.
     */
    public function set_is_background(bool $is_background): Push
    {
        $this->is_background = $is_background;
        return $this;
    }

    /**
     * Get the push notification object.
     *
     * @return array The push notification.
     */
    public function get_push(): array
    {
        $res = array();
        $res['data']['title'] = $this->title ?? "";
        $res['data']['is_background'] = $this->is_background ?? false;
        $res['data']['message'] = $this->message ?? "";
        $res['data']['image'] = $this->image ?? "";
        $res['data']['payload'] = $this->data ?? "";
        $res['data']['timestamp'] = date('Y-m-d G:i:s');
        return $res;
    }

    /**
     * Get a payload array.
     *
     * @param string $type The type of notification message.
     * @param array $parameters The parameters to be sent into the payload.
     * @return array An array containing the payload.
     * @pre $type should be a valid notification type (the ones defined as Push's  constants).
     * @post The payload array, given \f$n\in\mathbb{N}\f$ parameters, will have a field "message-type" containing
     * the message type and \f$n\f$ fields "param-i" containing the \f$i\f$-th parameter
     * (\f$i\in\mathbb{N}, i=0,...,n-1\f$). The following is an example of a valid payload array.
     * @code{.php}
     * array(
     *     "message-type" => "Message type",
     *     "param-0" => "Parameter 0",
     *     "param-1" => "Parameter 1",
     *      ...
     *     "param-n" => "Parameter n"
     * );
     * @endcode
     */
    public static function create_payload(string $type, array $parameters): array
    {
        $payload = array("message-type" => $type);
        for ($i = 0; $i < sizeof($parameters); $i++) {
            $payload["param-" . $i] = $parameters[$i];
        }
        return $payload;
    }

}