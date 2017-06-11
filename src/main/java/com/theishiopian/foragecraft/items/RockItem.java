package com.theishiopian.foragecraft.items;

import com.theishiopian.foragecraft.entity.EntityRockFlat;
import com.theishiopian.foragecraft.entity.EntityRockNormal;
import com.theishiopian.foragecraft.init.ModBlocks.RockType;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class RockItem extends ItemBlock
{
	RockType type;
	
	public RockItem(Block block, RockType t)
	{
		super(block);
		
		String name = null;
		
		switch(t)
		{
			case NORMAL: name = "rock_normal";
			break;
			case FLAT: name = "rock_flat";
			break;
		}
		
		type = t;
		
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.DECORATIONS);
	}
	
	public RockType getRockType()
	{
		return type;
	}
	
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
		switch(type)
		{
			case FLAT: target.attackEntityFrom(DamageSource.GENERIC, 2);
				break;
			case NORMAL: target.attackEntityFrom(DamageSource.GENERIC, 3);
				break;
			
		}
        return true;
    }

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote)
        {
        	EntityThrowable rock = new EntityRockNormal(worldIn, playerIn);
        	switch(type)
            {
            	
            	case NORMAL: rock = new EntityRockNormal(worldIn, playerIn);
            		break;
				case FLAT: rock = new EntityRockFlat(worldIn, playerIn);
					break;
				default:
					break;
            }
        	
        	
            rock.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(rock);
        }

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
	
	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
    {
		//anything will break faster if you hit it with a rock enough
		int e = 0;
		
		switch(type)
		{
			case FLAT: e = 2;
				break;
			case NORMAL: e = 3;
				break;
		}
		return e;
    }
}
