package it.polimi.deib.streams.oracle.s2r;

import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({WindowerWindowCloseTest.class, WindowerContentChangeTest.class})
public class WindowerTestSuite extends TestSuite{
}
