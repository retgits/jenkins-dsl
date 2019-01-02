workflow "New workflow" {
  on = "push"
  resolves = ["new-action"]
}

action "Shell" {
  uses = "actions/bin/sh@master"
  args = ["./scripts/validate.sh"]
}
