![example](assets/example.gif)

# jane

> Gather resources. Level up. Do quests.

## about

`jane`, named as a reference to the Ender's Game sequence of novels, was a bot
that played Old School RuneScape with one goal: to level all of the character's
skills to 99.

The contents of this repository represent a snapshot of `jane`'s code at some
interesting point in time. Given the pressure on third-party clients I decided
that I no longer had any interest in maintaining the code but thought that it
still had some academic interest.

I ran `jane` on half a dozen accounts, only one of which got banned. For what
it's worth, I didn't have any malicious intention with running these bots, I
just thought it was a cool side project to get into robotics. At the very
least, `jane` pushed down commmodity prices for a little while.

## the original

The code in the `old/` directory is from my original attempts to write hooks
for OSRS back in 2013. Some of them still work, but it's mostly there as an
  example for others to learn from.

## post-mortem

It actually wasn't that hard to do what I did given that there were so many
third-party clients around that provided nice API's for getting information
about the game world. I added the stuff that came after: routing the character
to its destination, clicking on entities, and abstracting away all of the
boring stuff like managing inventories and such. It got to the point where
spinning up a set of new functionality, like collecting flax or doing agility
courses, took just a few lines of code. It would have been really cool to keep
going but by that point I lost interest, heh.
