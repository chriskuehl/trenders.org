<?php
$query = $_GET['query'];
$contents = file_get_contents("http://trenders.org/stocksim/search/json?query=" . urlencode($query));
header("Content-type: application/json");
echo($contents);
?>