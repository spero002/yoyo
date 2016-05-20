<?php
require_once dirname(__FILE__) . '/../utils.php';
require_once dirname(__FILE__) . '/../config.php';

$ApiClient = new ApiClient();
$ApiClient->init(BASE_URL, PUBLIC_KEY, PRIVATE_KEY);

$Parameters = array('Action' => 'DescribeUDiskSnapshot',
    'Region' => 'cn-north-03'
);
$response = $ApiClient->get($Parameters);
#header('Content-type:text/json');
echo $response . "\n";

?>
