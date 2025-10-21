# ClapActivity - Simplified Clap Detection Flow

## Overview
ClapActivity là một activity mới với giao diện đơn giản hơn, tích hợp tất cả chức năng clap detection trong một màn hình duy nhất.

## Features

### 1. **Single Screen Design**
- Không sử dụng ViewPager hay multiple fragments
- Tất cả chức năng trong 1 activity duy nhất
- Giao diện đơn giản, dễ sử dụng

### 2. **Main Components**

#### **Activate/Deactivate Button**
- **Deactivated State**: Hiển thị `deactive.png` (màu xám)
- **Activated State**: Hiển thị `active.png` (màu xanh lá)
- Click để bật/tắt detection

#### **Sound Selection**
- Grid layout 3 cột hiển thị danh sách sounds
- Chọn sound để sử dụng khi phát hiện clap
- Sound được chọn có viền xanh và checkmark
- Tự động lưu vào AppPreferences

#### **Settings Button**
- Icon settings ở góc phải header "Sound"
- Mở SettingActivity để cấu hình Flash, Vibrate, Volume, Duration

### 3. **Detection Flow**

```
User clicks Activate Button
  ├─> Check Microphone Permission
  ├─> Check Notification Permission
  ├─> Start VocalService (Foreground Service)
  └─> Change UI to Active State

VocalService Running
  ├─> DetectClapClap listens for claps
  ├─> On 2 claps detected:
  │   └─> FeatureClapManager.playAll()
  │       ├─> Play selected sound
  │       ├─> Turn on flashlight (if enabled)
  │       └─> Vibrate (if enabled)
  └─> Auto stop after duration

User clicks Deactivate Button
  ├─> Stop VocalService
  ├─> Stop all sounds/flash/vibrate
  └─> Change UI to Inactive State
```

## Usage

### Open ClapActivity from anywhere:
```kotlin
ClapActivity.start(context)
```

### From HomeActivity:
```kotlin
binding.clapBtn.setOnClickListener {
    ClapActivity.start(this)
}
```

## File Structure

```
app/src/main/
├── java/.../main/activity/
│   └── ClapActivity.kt              # Main activity
├── java/.../main/adapter/
│   └── ClapSoundAdapter.kt          # Sound list adapter
└── res/
    ├── layout/
    │   ├── activity_clap.xml        # Main layout
    │   └── item_clap_sound.xml      # Sound item layout
    └── drawable/
        ├── active.png               # Active button image
        └── deactive.png             # Deactive button image
```

## Key Differences from MainActivity

| Feature | MainActivity | ClapActivity |
|---------|-------------|--------------|
| Architecture | ViewPager + 4 Fragments | Single Activity |
| Navigation | Bottom Navigation Bar | None |
| Sound Selection | Separate Fragment | Integrated in same screen |
| Settings | Separate Fragment | Opens SettingActivity |
| UI Complexity | Complex | Simple |
| Detection Logic | ✅ Same | ✅ Same |

## Technical Details

### Permissions Required
- `RECORD_AUDIO` - For clap detection
- `POST_NOTIFICATIONS` - For foreground service notification

### Services Used
- **VocalService** - Foreground service for background detection
- Same service as MainActivity

### Managers Used
- **FeatureClapManager** - Controls sound, flash, vibrate
- **DetectClapClap** - Audio processing for clap detection
- **AppPreferences** - Stores user settings

### BroadcastReceiver
- Listens to `ACTION_FINISH_DETECT` to stop detection
- Auto deactivate when notification is clicked

## Customization

### Change Active/Deactive Images
Replace in `drawable/`:
- `active.png` - Active state button
- `deactive.png` - Inactive state button

### Modify Sound Grid
Edit `activity_clap.xml`:
```xml
<androidx.recyclerview.widget.RecyclerView
    android:id="@+id/rcv_sounds"
    ... />
```

Change columns in `ClapActivity.kt`:
```kotlin
val gridLayoutManager = GridLayoutManager(this, 3) // Change 3 to desired columns
```

### Add More Sounds
Edit `AppRepository.getAllSound()` to add more sound items.

## Testing

1. Open app
2. Navigate to HomeActivity
3. Click "Clap" button
4. ClapActivity opens
5. Click activate button
6. Grant permissions if needed
7. Clap twice to test detection
8. Phone should ring with selected sound

## Notes

- ClapActivity sử dụng cùng logic detection với MainActivity
- Không ảnh hưởng đến MainActivity hiện tại
- Có thể sử dụng song song hoặc thay thế MainActivity
- All analytics events are logged with "clap_" prefix


