#!/bin/sh

pushd "$(dirname "$0")"
port="8390"
mvnexe="mvn"

while getopts "p:P:t:d" arg
do
        case $arg in
            p)
                port="$OPTARG"
                ;;
            d)
                mvnexe="mvnDebug -Dmaven.test.skip=true"
                ;;
            ?)
            echo "unkonw argument"
        exit 1
        ;;
        esac
done

mvn clean
$mvnexe -U jetty:run-exploded -Djetty.port=$port
popd
