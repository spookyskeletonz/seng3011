#!/usr/bin/perl -w

open( my $f => "fins_codes.txt") || die "Cannot open fins_codes.txt: $!";

my $exe = glob "target/*.jar";

while (my $line = <$f>) {
    my @args = ("java -cp ", $exe, " API 2005-10-01T00:00:00.000Z 2015-10-10T00:00:00.000Z - ", $line);
    my $output = `@args`;
    if (length $output > 700) {
        print $line;
    } 
}

print "\n";