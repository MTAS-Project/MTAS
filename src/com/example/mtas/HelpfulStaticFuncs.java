package com.example.mtas;


public class HelpfulStaticFuncs {
    public static String capitalize(final String str) {
        if (str.length() == 0) {
            return "";
        }

        final char[] buffer = str.toCharArray();
        buffer[0] = Character.toTitleCase(buffer[0]);

        for (int i = 1; i < buffer.length; i++) {
            if (buffer[i - 1] == ' ') {
                buffer[i] = Character.toTitleCase(buffer[i]);
            }
        }
        return new String(buffer);
    }
    public static void switchAutoSaveService(){
    	
    }

}
//	private MapData mapData;
//	
//	MTAS()
//	{
//		System.out.println("MTAS Object create!!");
//		mapData = new MapData();
//	}
	
//	public ArrayList<Reception> getAllData(Bound mapBound, String networkOp)
//	{
//		ArrayList<Reception> receptions;
//		
//		if(networkOp.matches("All Networks"))
//		{
//			receptions = mapData.getReceptions(mapBound);
//			System.out.println("All Operators");
//		}
//		else
//		{
//			receptions = mapData.getReceptions(mapBound,networkOp);
//		}
//		return receptions;
//	}
	
//	public ArrayList<Reception> getData(ArrayList<String> networks,ArrayList<String> services)
//	{
//		for(int i=0;i<services.size();i++)
//		{
//			System.out.println("MTAS = "+services.get(i));
//		}
//		return mapData.getReceptions(networks,services);
//	}
	
//	public ArrayList<Reception> getFilterData(String networkOp)
//	{
//		return mapData.getFilteredReceptions(networkOp);
//	}

