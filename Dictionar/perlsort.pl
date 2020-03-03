#!/usr/bin/perl

my $inputfilename = $ARGV[0];

open(FILE1, "<$inputfilename") or die "Can't read file $inputfilename\n";
my(@lines) = <FILE1>;
@lines = sort(@lines);
my($line);
foreach $line (@lines) {
  print "$line";
}
close(FILE1);


	