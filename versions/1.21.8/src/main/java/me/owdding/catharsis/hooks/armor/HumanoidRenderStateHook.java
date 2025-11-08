package me.owdding.catharsis.hooks.armor;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public interface HumanoidRenderStateHook {

    ThreadLocal<HumanoidRenderState> CURRENT_RENDER_STATE = new ThreadLocal<>();
}
