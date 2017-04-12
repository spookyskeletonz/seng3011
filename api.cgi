#!/usr/bin/perl -w
use CGI qw(:standard);
use strict;

my $query = new CGI;

my $start_date = $query->param('start_date');
my $end_date = $query->param('end_date');
my $instrument_id = $query->param('instrument_id');
my $topic_codes = $query->param('topic_codes');

if ($start_date eq "") {
    $start_date = "NULL";
}
if ($end_date eq "") {
    $end_date = "NULL";
}
if ($instrument_id eq "") {
    $instrument_id = "-";
}
if ($topic_codes eq "") {
    $topic_codes = "-";
}

my $input = join " ", $start_date, $end_date, $instrument_id, $topic_codes;
my $exe = glob "target/*.jar";
my @args = ("java -cp ", $exe, " API ", $input);
my $output = `@args`;

print $query->header("text/plain");
print "$output\n";
print "$start_date\n";
print "$end_date\n";
print "$instrument_id\n";
print "$topic_codes\n";