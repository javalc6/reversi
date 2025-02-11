# Reversi
The purpose of this project is to explore [Reversi](https://en.wikipedia.org/wiki/Reversi) (also known as Othello) game, which is a game based on a grid with eight rows and eight columns. 
This repository contains a collection of utilities to solve Reversi game.

In Reversi game each player must place a new piece in a position that there exists at least one straight (horizontal, vertical, or diagonal) line between the new piece and another piece of the same color, with one or more contiguous opposite pieces between them.

Black color starts first move. When player cannot move, the other player takes the turn. When neither players can move, the game ends. The winner is the player that owns more pieces.

Following java applications help finding interesting solutions in the Reversi domain:
* SymmetrySeeker, it looks for Reversi games ending with a _symmetric_ board using random algorithm.
* GameScanner, it looks for all Reversi games with upto _max_ moves with the option to filter only games ending with _symmetric_ board.

# Finding symmetric boards
The application SymmetrySeeker looks for random Reversi games ending with a _symmetric_ board. To compile it run following command:
```
javac SymmetrySeeker.java
```

To run the application use the following command:
```
java SymmetrySeeker [-v] [number of solutions]

-v verbose
```
If you don't specify the number of solutions, SymmetrySeeker will find 10 random solutions.

# Finding all games upto a defined maximum number of moves
The application GameScanner looks for all Reversi games with upto _max_ moves with the option to filter only games ending with _symmetric_ board. To compile it run following command:
```
javac GameScanner.java
```

To run the application use the following command:
```
java java GameScanner [-s] <max number of moves to explore> [<output filename>]

-s                Optional. If present, the search is restricted to only symmetric solutions.
<output filename> Optional. Specifies the file to write the found solutions to. 
```

# Example

Running SymmetrySeeker without parameters may provide following output:
```
Symmetric board with transcript = C4C3C2B2F5C1A1C5B5F4D3B4B3C6G4G6D2A4A5D1E2B1E3F3E1A6A3A2A7F6E6G5G7F2F1G2H2F7D6H6D7B6C7B8E7H7G8H3D8B7E8F8H4G3H5C8A8G1H8H1
Symmetric board with transcript = F5F4F3G4D3C4B3E6H4A2E7C2C6C5C3C7F6G7B5F2E2A5C8F1G5H5C1D8H8B4A4E3G6A3D2B6D6F7D7E8F8D1E1H3A6B1A1H7G1G2G8B7A7H6G3H2H1B8A8B2
Symmetric board with transcript = C4C5E6F5C6B5F6D3A5F7F3E3C3B7A8B3G5E2F1H5E7G3C2E8G7B4H3H8A3E1F4A2F8G4H4B6D6G6G8H2F2A4A6D7D8C8B2D2B1C1A1H7D1H6C7B8A7G2H1G1
Symmetric board with transcript = D3C3B3E3F3E2F5A3B2D6E1G2C5B4H1G5D7D8F6G3H3D2B5A5B6F4H5A7C1B1A1H2B7F2G1C2F1H6D1A2G4B8A4F7F8H4H7E7C7G7H8E6E8C4G8C8A6C6A8G6
Symmetric board with transcript = F5F6E6F4D3E7G5C4D8H5E3F7H6E2E1F3G7E8C5B5D2H7A5C1F2F1C3B3G1A6F8G4A2C6H4H3B7A8D6D7B4B2A4C7G8A3A7A1B6C8G6H8B1C2D1H1G2G3B8H2
Symmetric board with transcript = D3E3F5E6F3E2D1C4E7G5B3E1H5E8F4A2F1G4C5G6H7B5A5F6F7G7H6F2F8G8D7C2B1C8D6D2D8B4H8G3H3G1A3C1H1B6A7G2H2A4B2A6A1B7B8C6H4A8C7C3
Symmetric board with transcript = D3C3B3D2D1B2B1E3F3E2F6A3E1G7H8G3H3C1B4F4G4D6D7C5B5D8F2H2F5G5H4G2F1G1H1F7F8H6C2A5C7E7G6A1B6B7B8H5E6G8H7E8C8A7C4C6A8A6A4A2
Symmetric board with transcript = E6F4F3D6C4B4E3F2A4E7G1D3G4F5D7D8E2E1C5D2C2F6F7H4C7F8G5F1G3B5A5H3G2C8G7D1C3B3C1B6C6G6H5H7E8G8H8H6H2B2A2B1A1H1B8B7A6A8A3A7
Symmetric board with transcript = D3E3F5E6F3E2F7C3E1G3B2E7E8G6G4D6C6A1B3A3C4G8H3C2H5F6A2B6B1C1A6H7D2B4C5C7A4A5C8F2F4F8D1H4G5H6H8F1D8D7G7B5G1H1G2H2A7B7A8B8
Symmetric board with transcript = F5D6C3G5D7B2H5D8A1D3C4B3B4A2D2F4G4D1F6G3C5A4E3H4B5E6E7C2C1E8H2B6A3B1A5H6E2C6F7E1C7B7F2G7B8A7G6F3F8G8F1H7A8H3H8G1C8G2A6H1
```
Note that results change at each execution, as the algorithm is based on randomness.

Running GameScanner with parameters 9 and -s provides following output:
```
F5F4D3D6D7E3F3C5B5
F5D6C5F6E7F4G5E6E3
F5D6C5F4E7F6G5E6E3
F5D6C5F4E3F6G5E6E7
E6F4E3F6G5D6E7F5C5
E6F4E3D6G5F6E7F5C5
E6F4E3D6C5F6E7F5G5
E6D6C4F4G4C5C6E3E2
D3E3F5C5B5F4F3D6D7
D3C5D6E3F4C3D2C4B4
D3C5D6E3B4C3D2C4F4
D3C5D6C3B4E3D2C4F4
C4E3F4C5D6C3B4D3D2
C4E3F4C5D2C3B4D3D6
C4E3F4C3D2C5B4D3D6
C4C5E6E3E2D6C6F4G4
Number of found solutions: 16
```

# View the board using the transcript

To view the board corresponding to a transcript, you can use the following website, by replacing _transcript_ string with the effective value:
```
https://thesaurus.altervista.org/reversi?ts=transcript

e.g.

https://thesaurus.altervista.org/reversi?ts=C4C3E6F6G6D6C6E3E2F4G4C5C2
```

# References

- [Reversi](https://en.wikipedia.org/wiki/Reversi)

- [Reversi Playground](https://thesaurus.altervista.org/revers-game)

- [Reversi on Android](https://play.google.com/store/apps/details?id=livio.reversi)
