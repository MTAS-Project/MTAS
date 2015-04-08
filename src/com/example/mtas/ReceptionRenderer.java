package com.example.mtas;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class ReceptionRenderer extends DefaultClusterRenderer<Reception>{

	private static final int MIN_CLUSTER_SIZE = 60;
	
	public ReceptionRenderer(Context context, GoogleMap map, ClusterManager<Reception> clusterManager) {
		super(context, map, clusterManager);
		
		map.clear();
		clusterManager.clearItems();
	}
	
	@Override
	protected String getClusterText(int bucket) {
        
		super.getClusterText(bucket);
        return String.valueOf(bucket);
    }
	
	@Override
	protected int getBucket(Cluster<Reception> cluster) {
        super.getBucket(cluster);
        
        return cluster.getSize();
    }
	
	@Override
	protected boolean shouldRenderAsCluster(Cluster<Reception> cluster)
	{
		super.shouldRenderAsCluster(cluster);
		return cluster.getSize() > MIN_CLUSTER_SIZE;
	}
	

	@Override
	protected void onBeforeClusterItemRendered(Reception reception, MarkerOptions markerOptions)
	{
		super.onBeforeClusterItemRendered(reception, markerOptions);
		
	}
	
	@Override
	protected void onClusterItemRendered(Reception reception, Marker marker)
	{
		super.onClusterItemRendered(reception, marker);
		
		marker.setPosition(reception.getLocation());
		marker.setTitle(reception.getNetworkOp());
		String quality = MyCustomPhoneListener.levelToString(reception.getSignalStrength());
		marker.setSnippet(quality +","+reception.getServiceType()
						 +","+reception.getMaker()+","+reception.getModel());
		
		
//		System.out.print("Cluster ");reception.display();
//		System.out.println("MTAS signal strenght = "+reception.getSignalStrength()+","+reception.getServiceType());
		
		if(reception.getSignalStrength()==1)
		{
			switch(reception.getServiceType().toLowerCase())
			{
				case "g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.redg));
					break;
				}
				case "2g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.red2g));
					break;
				}
				case "3g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.red3g));
					break;
				}
				case "4g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.red4g));
					break;
				}
				case "e":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.rede));
					break;
				}
				case "h":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.redh));
					break;
				}
				case "h+":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.redhplus));
					break;
				}
			}
		}
		if(reception.getSignalStrength()==2)
		{
			switch(reception.getServiceType().toLowerCase())
			{
				case "g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.yellowg));
					break;
				}
				case "2g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.yellow2g));
					break;
				}
				case "3g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.yellow3g));
					break;
				}
				case "4g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.yellow4g));
					break;
				}
				case "e":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.yellowe));
					break;
				}
				case "h":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.yellowh));
					break;
				}
				case "h+":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.yellowhplus));
					break;
				}
			}
		}
		if(reception.getSignalStrength()==3)
		{
			switch(reception.getServiceType().toLowerCase())
			{
				case "g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.orangeg));
					break;
				}
				case "2g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.orange2g));
					break;
				}
				case "3g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.orange3g));
					break;
				}
				case "4g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.orange4g));
					break;
				}
				case "e":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.orangee));
					break;
				}
				case "h":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.orangeh));
					break;
				}
				case "h+":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.orangehplus));
					break;
				}
			}
		}
		if(reception.getSignalStrength()==4)
		{
			switch(reception.getServiceType().toLowerCase())
			{
				case "g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.greeng));
					break;
				}
				case "2g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green2g));
					break;
				}
				case "3g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green3g));
					break;
				}
				case "4g":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.green4g));
					break;
				}
				case "e":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.greene));
					break;
				}
				case "h":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.greenh));
					break;
				}
				case "h+":
				{
					marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.greenhplus));
					break;
				}
			}
		}
	}

}
