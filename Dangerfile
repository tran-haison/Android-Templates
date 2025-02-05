# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "WIP"

# Warn when there is a big PR
warn("Big PR") if git.lines_of_code > 500

# Warn to encourage a PR description
warn("Please provide a summary in the PR description to make it easier to review") if github.pr_body.length == 0

# Warn to encourage that labels should have been used on the PR
warn("Please add labels to this PR") if github.pr_labels.empty?

# Check commits lint and warn on all checks (instead of failing)
commit_lint.check warn: :all, disable: [:subject_length]

# Detekt output check
detekt_dir = "**/build/reports/detekt/detekt-result.xml" 
Dir[detekt_dir].each do |file_name|
  kotlin_detekt.skip_gradle_task = true
  kotlin_detekt.report_file = file_name
  kotlin_detekt.detekt(inline_mode: true)
end

# Android Lint output check
lint_dir = "**/**/build/reports/lint/lint-result.xml"
Dir[lint_dir].each do |file_name|
  android_lint.skip_gradle_task = true
  android_lint.report_file = file_name
  android_lint.lint(inline_mode: true)
end
  
# Show Danger test coverage report from Kover for templates
# Report coverage of modified files, warn if total project coverage is under 80%
# or if any modified file's coverage is under 90%
kover_file_template_xml = "template-xml/build/reports/kover/merged/xml/report.xml"
markdown "## Kover report for template-xml:"
shroud.reportKover "Template - XML Unit Tests", kover_file_template_xml, 80, 95, false

kover_file_template_compose = "template-compose/build/reports/kover/merged/xml/report.xml"
markdown "## Kover report for template-compose:"
shroud.reportKover "Template - Compose Unit Tests", kover_file_template_compose, 80, 95, false
