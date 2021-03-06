package re.domi.invisiblights;

import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PoweredLightRodItem extends LightRodItem
{
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new PoweredItemCapabilityProvider(Config.PoweredLightRodCapacity, stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        int energy = stack.getOrCreateTag().getInt("energy");
        tooltip.add(new TranslationTextComponent("tooltip.invisiblights.energy", energy, Config.PoweredLightRodCapacity, (100 * energy / Config.PoweredLightRodCapacity)));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack)
    {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack)
    {
        return 1D - stack.getOrCreateTag().getInt("energy") / (double) Config.PoweredLightRodCapacity;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack)
    {
        double durability = this.getDurabilityForDisplay(stack);
        return MathHelper.rgb(255 - (int) (durability * 127), 221 - (int) (durability * 110), 119 - (int) (durability * 59));
    }

    @Override
    public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items)
    {
        if (this.isInGroup(group))
        {
            items.add(new ItemStack(this));

            ItemStack full = new ItemStack(this);
            full.getOrCreateTag().putInt("energy", Config.PoweredLightRodCapacity);

            items.add(full);
        }
    }

    @Override
    public boolean canAffordLightSource(PlayerInventory inv, ItemStack heldItemStack)
    {
        return heldItemStack.getOrCreateTag().getInt("energy") >= Config.PoweredLightRodCost;
    }

    @Override
    public BlockState getPlacementBlockState(BlockState original)
    {
        return original.with(LightSourceBlock.POWERED, true);
    }

    @Override
    public void postPlace(PlayerInventory inv, ItemStack heldItemStack)
    {
        CompoundNBT nbt = heldItemStack.getOrCreateTag();
        nbt.putInt("energy", nbt.getInt("energy") - Config.PoweredLightRodCost);
    }
}
