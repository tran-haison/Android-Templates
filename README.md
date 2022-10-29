<p align="center">
  <a href="https://nimblehq.co/">
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://assets.nimblehq.co/logo/dark/logo-dark-text-320.png">
      <img alt="Nimble logo" src="https://assets.nimblehq.co/logo/light/logo-light-text-320.png">
    </picture>    
  </a>
</p>

---

Our Android template: **[template](https://github.com/nimblehq/android-templates/tree/develop/template)**

## Setup

1. Clone or download this repository to your local machine, then extract and open the folder
2. Install [Kotlin](https://github.com/JetBrains/kotlin) v1.6.21
3. Install [Kscript](https://github.com/holgerbrandl/kscript#installation) v4.0.3
4. Run `cd scripts` to get into the scripts directory
5. Run `kscript new_project.kts` to create a new project with the following arguments:
  ```   
    package-name=                      New package name (i.e., com.example.package)
    app-name=                          New app name (i.e., MyApp, "My App", "my-app")
  ```

  Example: `kscript new_project.kts package-name=co.myproject.example app-name="My Project"`

6. Update `android_version_code` and `android_version_name` in `template/build.gradle`

## Wiki

Learn more about our Android templates on the [Wiki](https://github.com/nimblehq/android-templates/wiki).

## License

This project is Copyright (c) 2014 and onwards Nimble. It is free software and may be redistributed under the terms specified in the [LICENSE] file.

[LICENSE]: /LICENSE

## About
<a href="https://nimblehq.co/">
  <picture>
    <source media="(prefers-color-scheme: dark)" srcset="https://assets.nimblehq.co/logo/dark/logo-dark-text-160.png">
    <img alt="Nimble logo" src="https://assets.nimblehq.co/logo/light/logo-light-text-160.png">
  </picture>
</a>

This project is maintained and funded by Nimble.

We ❤️ open source and do our part in sharing our work with the community!
See [our other projects][community] or [hire our team][hire] to help build your product.

Want to join? [Check out our Jobs][jobs]!

[community]: https://github.com/nimblehq
[hire]: https://nimblehq.co/
[jobs]: https://jobs.nimblehq.co/
