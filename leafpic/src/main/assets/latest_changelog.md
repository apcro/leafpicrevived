v0.8
==================
- Moving out of 'alpha'
- Update some build libraries

v0.8-alpha-3
==================
- Fixed a bug with deleting images and albums
- Removed the Billing permission request
- App now available on Google Play at https://play.google.com/store/apps/details?id=com.alienpants.leafpic

v0.8-alpha-2
==================
- The Liz library has been removed, and the library itself has been upgraded to AndroidX and added to Jitpack
- A number of the outstanding pull requests on Gitlab have been manually merged
  - https://gitlab.com/HoraApps/LeafPic/merge_requests/598
  - https://gitlab.com/HoraApps/LeafPic/merge_requests/593
- An issue on Gitlab have been fixed
  - https://gitlab.com/HoraApps/LeafPic/issues/606
- Pull to Refresh on the Albums page now correctly loads all albums, including Excluded albums recently removed from the exclusion list
- The albums page landing animator has been re-enabled
- Remove Rate and Translate links for now
- Update Report bug link
- Update Github link

v0.8-alpha-1
==================
- The source has been updated to support androidx and API 28+
- The original HoraApp Liz theme library has also been upgraded and included in the source tree rather than as a library
- All associated libraries have been upgraded to use their current versions
- This is not a fork so much as a copy+convert, and as the original LeafPic has been abandonded, this has been moved to a new organisation
- This includes a new GitHub issues tracker at https://github.com/apcro/leafpic/issues
- Donate links have been removed, as has the experimental Wallpapers and Timeline functions. These may return

v0.6-beta-1
==================
- Fixed crash on startup and some random crash
- Fixed crash opening video (Nougat)
- fixed zoom out issue with SubScaling ImageView enabled
- Updated translations
- General improvements