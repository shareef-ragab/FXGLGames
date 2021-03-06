package com.almasb.fxglgames.slotmachine.components;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.almasb.fxglgames.slotmachine.SlotMachineApp;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class LeverComponent extends Component {

    private ViewComponent view;
    private String currentTextureName = "lever0.png";

    @Override
    public void onAdded() {
        view.addClickListener(this::trigger);
    }

    public void trigger() {
        if (FXGL.<SlotMachineApp>getAppCast().isMachineSpinning())
            return;

        currentTextureName = currentTextureName.equals("lever0.png") ? "lever1.png" : "lever0.png";

        view.setView(FXGL.texture(currentTextureName));

        FXGL.<SlotMachineApp>getAppCast().spin();
    }
}
