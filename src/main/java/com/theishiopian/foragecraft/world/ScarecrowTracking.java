package com.theishiopian.foragecraft.world;

import java.util.LinkedHashSet;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.util.Constants;

// Fuckin hate the scarecrow problems
public class ScarecrowTracking extends WorldSavedData
{

	private final LinkedHashSet<BlockPos>scarecrowLocations = new LinkedHashSet<BlockPos>();
	private final static String NAME = "foragecraft:scarecrows";

	public ScarecrowTracking()
	{
		super(NAME);
	}

	public static ScarecrowTracking get(World world) 
	{
		// The IS_GLOBAL constant is there for clarity, and should be simplified into the right branch.
		MapStorage storage = world.getPerWorldStorage();
		ScarecrowTracking instance = (ScarecrowTracking)storage.getOrLoadData(ScarecrowTracking.class, NAME);

		if (instance == null)
		{
			instance = new ScarecrowTracking();
			storage.setData(NAME, instance);
		}
		return instance;
	}

	// adds scarecrow to list
	public void addScarecrow(BlockPos pos)
	{
		scarecrowLocations.add(pos);
		System.out.println("Registering Scarecrow");
	}

	// removes scarecrow from list
	public void removeScarecrow(BlockPos pos)
	{
		scarecrowLocations.remove(pos);
		System.out.println("Unregistering Scarecrow");
	}

	@Override
	public void readFromNBT(NBTTagCompound scTag) 
	{
		scarecrowLocations.clear();

		final NBTTagList scarecrows = scTag.getTagList("Scarecrows", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < scarecrows.tagCount(); i++) 
		{
			final NBTTagCompound locationTag = scarecrows.getCompoundTagAt(i);

			final BlockPos location = new BlockPos(locationTag.getInteger("posX"), locationTag.getInteger("posY"), locationTag.getInteger("posZ"));

			scarecrowLocations.add(location);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound scTag) 
	{
		final NBTTagList locationList = new NBTTagList();

		for (BlockPos pos : scarecrowLocations) 
		{
			final NBTTagCompound locationTag = new NBTTagCompound();
			locationTag.setInteger("posX", pos.getX());
			locationTag.setInteger("posY", pos.getY());
			locationTag.setInteger("posZ", pos.getZ());
			locationList.appendTag(locationTag);
		}
		scTag.setTag("Scarecrows", locationList);

		return scTag;
	}

	public boolean inRange(Entity entity)
	{
		for(BlockPos pos : scarecrowLocations)
		{
			if(entity.getEntityBoundingBox().intersects(new AxisAlignedBB(pos).expand(64,64,64)))
			{
				return true;
			}
		}
		return false;
	}
}