del "SquareRemover.jar"
copy "D:\TopCoder\Marathon Match\SquareRemover\dist\SquareRemover.jar" "D:\TopCoder\Marathon Match\SquareRemover\"
java -Xmx1024m -jar SquareRemoverVis.jar -exec "java -jar SquareRemover.jar" -seed 5 -pause