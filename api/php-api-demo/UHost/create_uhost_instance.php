<?php
ini_set('display_errors', '1');
require_once dirname(__FILE__) . '/../utils.php';
require_once dirname(__FILE__) . '/../config.php';

$ApiClient = new ApiClient();
$ApiClient->init(BASE_URL, PUBLIC_KEY, PRIVATE_KEY);

$Parameters = array('Action' => 'CreateUHostInstance',
    'Region' => 'cn-north-01',
    'ImageId' => 'uimage-fig4tz',
    'LoginMode' => 'Password',
    'Password' => 'MTIzNDU2NZgk',
    'Tag' => 'hsium.gao'
);

$response = $ApiClient->get($Parameters);
echo $response;

?>
