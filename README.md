# AlkisuDatahub Android App

Native WebView wrapper for alkisudatahub.com.ng — splash screen, onboarding,
biometric unlock, push notifications (OneSignal), and automatic download of
NIN/BVN verification slips. Built and compiled entirely from a phone, no PC
needed — GitHub does the compiling.

## 1. Upload this project

Unzip this folder, then in the GitHub app or github.com on your phone browser:
create the repo `alkisudatahub-app` (private) and upload every file/folder here,
keeping the exact folder structure (`app/`, `.github/`, `build.gradle`, etc).

Easiest path: open the repo on github.com → "Add file" → "Upload files" →
select everything from the unzipped folder in one go.

## 2. Set your OneSignal App ID

Open `app/src/main/res/values/strings.xml` in the GitHub web editor (tap the
pencil icon on the file) and replace:

```
<string name="onesignal_app_id">YOUR-ONESIGNAL-APP-ID-HERE</string>
```

with the real App ID from https://dashboard.onesignal.com/apps/ → your app →
Settings → Keys & IDs. Commit the change.

## 3. Get a debug APK (fast, for testing)

Go to the **Actions** tab → **Debug Build** → **Run workflow**. Wait for the
green check, open the run, and download the `alkisudatahub-debug-apk`
artifact — that's your installable `.apk`. Good for testing on your own
phone (you may need to allow "install unknown apps" once).

## 4. Create a signing key (once, before your first real release)

Go to **Actions** → **Generate Release Keystore** → **Run workflow**. When
it finishes, download the `release-keystore` artifact — this is your
`alkisudatahub-release.jks` signing key. **Keep this file safe forever**:
every future update to the Play Store must be signed with the same key, or
Google will reject it.

Then, still from your phone:
1. Base64-encode the `.jks` file. You can do this in a Codespace terminal:
   `base64 -w0 alkisudatahub-release.jks` — copy the long output string.
2. Go to your repo → **Settings** → **Secrets and variables** → **Actions**
   → **New repository secret**, and add these four secrets:
   - `KEYSTORE_BASE64` — the long base64 string from step 1
   - `KEYSTORE_PASSWORD` — the password you used when generating the keystore
   - `KEY_ALIAS` — `alkisudatahub` (unless you changed it)
   - `KEY_PASSWORD` — same as `KEYSTORE_PASSWORD` unless you changed it

Also open `generate-keystore.yml` and change `"ChangeThisPassword123!"`
to your own password **before** running it, or use the default and record it
exactly as your `KEYSTORE_PASSWORD`/`KEY_PASSWORD` secrets.

## 5. Get your signed release APK

Go to **Actions** → **Signed Release Build** → **Run workflow**. Download
the `alkisudatahub-release-apk` artifact when it's done — this is the file
you distribute or upload to the Play Store.

## What's already wired in

- Splash screen with your logo
- 3-slide onboarding: Welcome to AlkisuDatahub / Buy Your Cheap Data / Do
  Your Verification (edit wording in `strings.xml`)
- Biometric (fingerprint/face) unlock before the site loads, with a
  fallback straight to your site's own email/password login
- WebView loads `https://alkisudatahub.com.ng` directly — no separate login
  screen to maintain, your PHP site's login is the one true login
- Any file the site serves for download (NIN slip, BVN slip, receipts)
  downloads automatically to the phone's Downloads folder — no extra tap
- OneSignal push notification SDK initialized (just needs your App ID)
- Pull-to-refresh on the WebView

## Still to do on your side

- Swap the placeholder icon shapes in `res/drawable/` for anything closer
  to your brand if you want something other than the simple line icons
- If you want push notifications to actually fire when a slip/order is
  ready, wire an OneSignal REST API call into your PHP backend (I can help
  with that PHP code separately — just ask)
