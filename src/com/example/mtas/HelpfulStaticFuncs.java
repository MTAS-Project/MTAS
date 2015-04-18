package com.example.mtas;


public class HelpfulStaticFuncs {

	public static String networkTypeIntToString(int networkType) {
		switch (networkType) {
			case 1: {
				return "G"; // GRPS
			}
			case 2: {
				return "E"; // EDGE
			}
			case 11: {
				return "2G";//
			}
			case 3: {
				return "3G";// 3G
			}
			case 10: {
				return "H";//
			}
			case 13: {
				return "4G";//
			}
			case 15: {
				return "H+";//
			}
			default: {
				return "Unkown(" + networkType + ")"; // cater for all unknown types
			}
		}
	}

	public static String asuToString(final int asu) {
		if (asu <= 2 || asu == 99) {
			return "No service";
		} else if (asu > 2 && asu <= 5) {
			return "Weak";
		} else if (asu > 5 && asu <= 8) {
			return "Fair";
		} else if (asu > 8 && asu <= 12) {
			return "Good";
		} else if (asu > 12) {
			return "Excellent";
		} else { // unknown signal strength
			return "Unknown("+asu+")"; // in case appear
		}
	}
	public static String levelToString(final int level) {
		if (level == 0) {
			return "No service";
		} else if (level == 1) {
			return "Weak";
		} else if (level == 2) {
			return "Fair";
		} else if (level == 3) {
			return "Good";
		} else if (level == 4) {
			return "Excellent";
		} else { // unknown signal strength
			return "Unknown("+level+")"; // in case appear
		}
	}

	public static int asuToLevel(final int asu) {
		if (asu <= 2 || asu == 99) {
			return 0;
		} else if (asu > 2 && asu <= 5) {
			return 1;
		} else if (asu > 5 && asu <= 8) {
			return 2;
		} else if (asu > 8 && asu <= 12) {
			return 3;
		} else if (asu > 12) {
			return 4;
		} else { // this should not appear
			return -1; // -1 should be interpreted as 'unknown signal strength'
		}
	}

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

	public static void switchAutoSaveService() {

	}

}
// private MapData mapData;
//
// MTAS()
// {
// System.out.println("MTAS Object create!!");
// mapData = new MapData();
// }

// public ArrayList<Reception> getAllData(Bound mapBound, String networkOp)
// {
// ArrayList<Reception> receptions;
//
// if(networkOp.matches("All Networks"))
// {
// receptions = mapData.getReceptions(mapBound);
// System.out.println("All Operators");
// }
// else
// {
// receptions = mapData.getReceptions(mapBound,networkOp);
// }
// return receptions;
// }

// public ArrayList<Reception> getData(ArrayList<String>
// networks,ArrayList<String> services)
// {
// for(int i=0;i<services.size();i++)
// {
// System.out.println("MTAS = "+services.get(i));
// }
// return mapData.getReceptions(networks,services);
// }

// public ArrayList<Reception> getFilterData(String networkOp)
// {
// return mapData.getFilteredReceptions(networkOp);
// }

