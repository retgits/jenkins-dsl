workflow "New workflow" {
  on = "push"
  resolves = ["Shell"]
}

action "Shell" {
  uses = "actions/bin/sh@master"
  args = ["./scripts/validate.sh"]
}
