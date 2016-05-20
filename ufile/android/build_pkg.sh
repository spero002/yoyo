#!/bin/bash

lang="android"

cd ..
tar zcvf "$lang"_sdk.tar.gz ./* --exclude build_pkg.sh --exclude .gitignore
cd -
