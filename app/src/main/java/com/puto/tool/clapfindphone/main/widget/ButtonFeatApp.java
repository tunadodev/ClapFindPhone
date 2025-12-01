package com.puto.tool.clapfindphone.main.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.puto.tool.clapfindphone.R;
import com.puto.tool.clapfindphone.databinding.CustomButtonFeatAppBinding;

public class ButtonFeatApp extends FrameLayout {

    private CustomButtonFeatAppBinding binding;
    private int gradientStartColor;
    private int gradientEndColor;
    private int gradientCenterColor;
    private float cornerRadius;
    private boolean hasCenterColor = false;
    private float buttonIconSize = 0f;
    private float buttonIconTranslationX = 0f;
    private float buttonIconTranslationY = 0f;
    private float buttonIconMarginTop = 0f;

    public ButtonFeatApp(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ButtonFeatApp(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ButtonFeatApp(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Inflate layout using view binding
        binding = CustomButtonFeatAppBinding.inflate(LayoutInflater.from(context), this, true);

        // Default values
        gradientStartColor = context.getColor(com.jrm.R.color.blue_500);
        gradientEndColor = context.getColor(R.color.color_orange);
        gradientCenterColor = 0;
        cornerRadius = context.getResources().getDimension(R.dimen.default_corner_radius);

        // Get attributes
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonFeatApp);
            
            String buttonText = typedArray.getString(R.styleable.ButtonFeatApp_buttonText);
            int iconResId = typedArray.getResourceId(R.styleable.ButtonFeatApp_buttonIcon, -1);
            buttonIconSize = typedArray.getDimension(R.styleable.ButtonFeatApp_buttonIconSize, 0f);
            buttonIconTranslationX = typedArray.getDimension(R.styleable.ButtonFeatApp_buttonIconTranslationX, 0f);
            buttonIconTranslationY = typedArray.getDimension(R.styleable.ButtonFeatApp_buttonIconTranslationY, 0f);
            buttonIconMarginTop = typedArray.getDimension(R.styleable.ButtonFeatApp_buttonIconMarginTop, 0f);
            
            gradientStartColor = typedArray.getColor(
                    R.styleable.ButtonFeatApp_gradientStartColor, 
                    gradientStartColor
            );
            
            gradientEndColor = typedArray.getColor(
                    R.styleable.ButtonFeatApp_gradientEndColor, 
                    gradientEndColor
            );
            
            if (typedArray.hasValue(R.styleable.ButtonFeatApp_gradientCenterColor)) {
                gradientCenterColor = typedArray.getColor(
                        R.styleable.ButtonFeatApp_gradientCenterColor, 
                        0
                );
                hasCenterColor = true;
            }
            
            cornerRadius = typedArray.getDimension(
                    R.styleable.ButtonFeatApp_cornerRadius, 
                    cornerRadius
            );

            // Set text
            if (buttonText != null) {
                binding.buttonText.setText(buttonText);
            }

            // Set icon
            if (iconResId != -1) {
                binding.buttonIcon.setImageResource(iconResId);
            }

            // Apply optional icon size & positioning
            ViewGroup.LayoutParams lp = binding.buttonIcon.getLayoutParams();
            if (buttonIconSize > 0f) {
                lp.width = (int) buttonIconSize;
                lp.height = (int) buttonIconSize;
            }
            if (lp instanceof ConstraintLayout.LayoutParams && buttonIconMarginTop != 0f) {
                ConstraintLayout.LayoutParams clp = (ConstraintLayout.LayoutParams) lp;
                clp.topMargin = (int) buttonIconMarginTop;
                lp = clp;
            }
            binding.buttonIcon.setLayoutParams(lp);

            // Apply optional icon translation
            if (buttonIconTranslationX != 0f) {
                binding.buttonIcon.setTranslationX(buttonIconTranslationX);
            }
            if (buttonIconTranslationY != 0f) {
                binding.buttonIcon.setTranslationY(buttonIconTranslationY);
            }

            typedArray.recycle();
        }

        // Apply gradient
        applyGradient();
    }

    private void applyGradient() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(cornerRadius);
        
        if (hasCenterColor) {
            // Three color gradient
            int[] colors = {gradientStartColor, gradientCenterColor, gradientEndColor};
            gradientDrawable.setColors(colors);
        } else {
            // Two color gradient
            int[] colors = {gradientStartColor, gradientEndColor};
            gradientDrawable.setColors(colors);
        }
        
        gradientDrawable.setOrientation(GradientDrawable.Orientation.TL_BR);
        binding.buttonContainer.setBackground(gradientDrawable);
    }

    // Setter methods for dynamic updates
    public void setButtonText(String text) {
        binding.buttonText.setText(text);
    }

    public void setButtonIcon(int iconResId) {
        binding.buttonIcon.setImageResource(iconResId);
    }

    public void setGradientColors(int startColor, int endColor) {
        this.gradientStartColor = startColor;
        this.gradientEndColor = endColor;
        this.hasCenterColor = false;
        applyGradient();
    }

    public void setGradientColors(int startColor, int centerColor, int endColor) {
        this.gradientStartColor = startColor;
        this.gradientCenterColor = centerColor;
        this.gradientEndColor = endColor;
        this.hasCenterColor = true;
        applyGradient();
    }

    public void setCornerRadius(float radius) {
        this.cornerRadius = radius;
        applyGradient();
    }
}
