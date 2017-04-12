#!/usr/bin/perl -w
# written by andrewt@cse.unsw.edu.au September 2016
# as a starting point for COMP2041/9041 assignment 2
# http://cgi.cse.unsw.edu.au/~z5059978/seng3011/

use CGI qw/:all/;
use CGI::Carp qw/fatalsToBrowser warningsToBrowser/;
use DateTime;


sub main() {
    # print start of HTML ASAP to assist debugging if there is an error in the script
    print page_header();

    # Now tell CGI::Carp to embed any warning in HTML
    warningsToBrowser(1);

	$start = param('Start');
	$end = param('End');
	$instrument = param('instrument');
	$topic = param('topic');
	

    if (defined $start && defined $end && defined $instrument && defined $topic){
    	show_result();
	} else {
		enter_query();
	}

    
	print page_footer();
}

sub enter_query{
	print <<eof;
	<form method="POST" action="">
		Start Date (layout: yyyy-MM-ddTHH:mm:ss.SSSZ) <br>
		<input type"text" name="Start" placeholder="yyyy-MM-ddTHH:mm:ss.SSSZ"> <br>
		End Date (layout:  yyyy-MM-ddTHH:mm:ss.SSSZ):<br>
		<input type="text" name="End" placeholder="yyyy-MM-ddTHH:mm:ss.SSSZ"> <br>
		Instrument Id (layout: Exchange Ticker code + . + Exchange Identifier Code):<br>
		<input type="text" name="instrument" placeholder="ANZ.AX, WOW.AX"> <br>
		Topic Code (layout:  unique topic code ie. ECB):<br>
		<input type="text" name="topic" placeholder="XXX"> <br>
		<input type="submit" value="Search">
	</form>
	<br>
eof
}

sub show_result{
	$input = "start_date=".$start."\&end_date=".$end."\&instrument_id=".$instrument."\&topic_codes=".$topic;
	$url = 	"http://139.59.224.37/api/api.cgi?".$input;
	print <<eof
	You can find the output here: <br>
	<a href=$url>here</a>
	<F>
	<form method="POST" action="">
		<input type="submit" value="Return">
	</form>
eof
}

#
# HTML placed at the top of every page
#
sub page_header {
    return <<eof
Content-Type: text/html

<!DOCTYPE html>
<html lang="en">
<head>
<link href="matelook.css?ts=<?=time()?>&quot" rel="stylesheet">
<title>itsGifnotGif</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="http://www.w3schools.com/lib/w3.css">
<link rel="stylesheet" href="http://www.w3schools.com/lib/w3-theme-blue-grey.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Open+Sans'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">
<style>
html,body,h1,h2,h3,h4,h5 {font-family: "Open Sans", sans-serif}
</style>
<body class="w3-theme-l5">

eof
}

#
# HTML placed at the bottom of every page
# It includes all supplied parameter values as a HTML comment
# if global variable $debug is set
#
sub page_trailer {
    my $html = "";
    $html .= join("", map("<!-- $_=".param($_)." -->\n", param())) if $debug;
    $html .= end_html;
    return $html;
}


sub page_footer{
	return <<eof;
	    <!-- End Right Column -->
    </div>
      <!-- End Grid -->
  </div>
  
<!-- End Page Container -->
</div>
<br>

<!-- Footer -->
<footer class="w3-container w3-theme-d3 w3-padding-16">
</footer>

<footer class="w3-container w3-theme-d5">
</footer>
eof
}

main();
