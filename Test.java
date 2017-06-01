    package test;
    
    public class Test {
    	public static void main(String[] args) {
    		String value = test();
    		System.out.println(value);
    	}
    
    	private static String test() {
    		TestClosable testClosable = new TestClosable();
    		try {
    			System.out.println("In try block.");
    			return TestingReturn.getData();
    		} catch (Exception e) {
    			e.printStackTrace();
    			return e.getMessage();
    		} finally {
    			try {
    				testClosable.close();
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }
    
    class TestClosable implements AutoCloseable {
    
    	@Override
    	public void close() throws Exception {
    		System.out.println("TestClosable is closed just now.");
    	}
    }
    
    class TestingReturn {
    
    	public static String getData() {
    		System.out.println("Executing return statement.");
    		return "Data";
    	}
    }
