@ECHO OFF
:: Antarctica powered by BCSV
TITLE Run Antarctica BCSV
ECHO Setting Javafx path...
set FX="%cd%\javafx-sdk-14.0.1\lib"
ECHO Starting program
java -jar --module-path %FX% --add-modules javafx.controls bcsv_11.jar
ECHO Exiting...