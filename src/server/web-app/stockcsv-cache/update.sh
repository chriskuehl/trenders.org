#!/bin/sh
wget -O "nasdaq-$(date +%Y%m%d).csv" "http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=nasdaq&render=download"
wget -O "nyse-$(date +%Y%m%d).csv" "http://www.nasdaq.com/screening/companies-by-name.aspx?letter=0&exchange=nyse&render=download"
