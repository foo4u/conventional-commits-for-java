File touchFile = new File( basedir, "target/touch.txt" );
File versionFile = new File( basedir, "target/version.txt" );

assert touchFile.isFile()
assert versionFile.isFile()
