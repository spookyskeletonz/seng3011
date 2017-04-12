#!/usr/bin/perl -w
use CGI qw(:standard);
use strict;

my $query = new CGI;

my $start_date = $query->param('start_date');
my $end_date = $query->param('end_date');
my $instrument_id = $query->param('instrument_id');
my $topic_codes = $query->param('topic_codes');

my $input = join " ", $start_date, $end_date, $instrument_id, $topic_codes;
my @args = ("java -jar target/executable.jar", $input);
my $output = `@args`;

print $query->header("text/plain");
print "$output\n";