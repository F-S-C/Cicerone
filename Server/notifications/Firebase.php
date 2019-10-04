<?php

namespace notifications;

require_once "/home/fsc/www/notifications/Push.php";

/**
 * A controller class to communicate with Firebase.
 */
class Firebase
{
    /** @var string The Firebase API key. */
    private const FIREBASE_API_KEY = 'AAAAiwwZvhM:APA91bFKrWg26faVaZRfjubtDxjN79R8tHN5PLoDNwnw5BdpR6CrUDXW24puraBpvfHtmj3BEy-t7XxqIABM_L-3zjk4AmOC9pZ8gt-weqm78K27_4qVPXKhyUq8jald1xerDl-lioIZ';

    /**
     * Send a push message to a single user, using his Firebase ID.
     *
     * @param string $to The Firebase ID.
     * @param Push $message The Push message.
     * @return string The result of the operation.
     */
    public function send(string $to, Push $message): string
    {
        $fields = array(
            'to' => $to,
            'data' => $message,
        );
        return $this->send_push_notification($fields);
    }

    /**
     * Send a push message to a Firebase topic (a group to which users can subscribe/unsubscribe).
     *
     * @param string $to The Firebase topic's name.
     * @param Push $message The Push message.
     * @return string The result of the operation.
     */
    public function send_to_topic(string $to, Push $message): string
    {
        $fields = array(
            'to' => '/topics/' . $to,
            'data' => $message->get_push(),
        );
        return $this->send_push_notification($fields);
    }

    /**
     * Send a push notification.
     *
     * @param array $fields The fields that define the notification as Firebase needs.
     * @return string The result of the operation.
     */
    private function send_push_notification(array $fields): string
    {
        $url = 'https://fcm.googleapis.com/fcm/send';

        $headers = array(
            'Authorization: key=' . self::FIREBASE_API_KEY,
            'Content-Type: application/json'
        );

        $ch = curl_init();

        // Set the url, number of POST vars, POST data
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        // Disabling SSL Certificate support temporarily
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));

        // Execute post
        $result = curl_exec($ch);
        if ($result === false) {
            die('Curl failed: ' . curl_error($ch));
        }

        // Close connection
        curl_close($ch);

        return (string)$result;
    }
}