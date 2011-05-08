#!/bin/sh
find . -name .svn -prune -o -name .git -prune -o -type f -print0 \
  | xargs -0 grep -ZIl . \
  | xargs -0 perl -ni -e 'chomp; print ($_,"\n")'
