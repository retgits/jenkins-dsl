workflow "New workflow" {
  on = "push"
  resolves = ["Shell"]
}

action "Shell" {
  uses = "retgits/actions/sh@master"
  args = ["./scripts/validate.sh"]
}
