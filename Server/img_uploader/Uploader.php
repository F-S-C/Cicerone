<?php

namespace img_uploader;

/**
 * Class Uploader. It takes care of creating the image file from a base64 string.
 */
class Uploader
{
    /** @var string The upload path on the server. */
    protected const UPLOAD_PATH = "/home/fsc/www/images/";

    /**
     * @var string The image string.
     */
    private $image;

    /**
     * Uploader constructor.
     */
    public function __construct()
    {
        if (isset($_POST['image'])) {
            $this->image = $_POST['image'];
            $this->upload();
        } else {
            print('{"result":false,"error":"Missing image!"}');
        }
    }

    /**
     * Uploader destructor.
     */
    public function __destruct()
    {
        // Do nothing.
    }

    /**
     * Check that a file with the same name is not present.
     *
     * @param string $filename Filename to search in the image folder.
     * @return bool True if the file is present, otherwise false.
     */
    public function verifyIfExists(string $filename): bool
    {
        $pkt = self::UPLOAD_PATH . $filename;
        return file_exists($pkt . ".png")
            || file_exists($pkt . ".jpg")
            || file_exists($pkt . ".jpeg");
    }

    /**
     * Generate a random name.
     *
     * @return string The name generated.
     */
    public function generateName(): string
    {
        do {
            $id = uniqid(time() . '-');
        } while ($this->verifyIfExists($id));
        return $id;
    }

    /**
     * Convert a base64 string into an image and move it to the images folder.
     */
    private function upload(): void
    {
        $name = $this->generateName();
        $complete_path = self::UPLOAD_PATH . $name . ".jpg";
        $saved_file = file_put_contents($complete_path, base64_decode($this->image));
        if (($saved_file === false) || ($saved_file == -1)) {
            print('{"result":false,"error":"Couldn\'t save the image!"}');
        } else {
            print('{"result":true,"message":"' . $name . '.jpg"}');
        }
    }
}

$connector = new Uploader();