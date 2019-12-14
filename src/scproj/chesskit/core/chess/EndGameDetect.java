package scproj.chesskit.core.chess;

import scproj.chesskit.core.data.PlayerSide;

import java.util.List;

public class EndGameDetect extends EndGameDetectBasic{
    public static PlayerSide getWinner(ChessGrid chessGrid) {
        PlayerSide side=getWinnerBasic(chessGrid);
        if(side!=null) return side;
        ChessGridElement[][] grid = chessGrid.getGrid();
        List<CanMove> listBlack = CanMoveList.GetBlackCanMove(chessGrid);//获取黑方所有能走的步骤
        List<CanMove> listRed = CanMoveList.GetRedCanMove(chessGrid);     //获取红方所有能走的步骤
        if(listBlack.size()==0) return PlayerSide.RED;
        if(listRed.size()==0) return PlayerSide.BLACK;
        for (CanMove click : listBlack) {
            if (grid[click.endi][click.endj] == ChessGridElement.RED_GENERAL) {
                int GPx = click.endi;             //将的位置x
                int GPy = click.endj;             //将的位置y
                boolean t = false;

                for (CanMove reply : listRed) {
                    if (reply.endi == click.starti && reply.endj == click.startj) {
                        t = true;
                        break;
                    }
                }
                if (t) continue;

                t = true;
                if (Rule.movePieceMove(grid,ChessGridElement.RED_GENERAL, GPx, GPy, GPx - 1, GPy)) {
                    for (CanMove reply : listBlack) {
                        if (reply.endi == GPx - 1 && reply.endj == GPy) {
                            t = false;
                            break;
                        }
                    }
                } else t = false;
                if (t) continue;

                t = true;
                if (Rule.movePieceMove(grid,ChessGridElement.RED_GENERAL, GPx, GPy, GPx + 1, GPy)) {
                    for (CanMove reply : listBlack) {
                        if (reply.endi == GPx + 1 && reply.endj == GPy) {
                            t = false;
                            break;
                        }
                    }
                } else t = false;
                if (t) continue;

                if (Rule.movePieceMove(grid,ChessGridElement.RED_GENERAL, GPx, GPy, GPx, GPy - 1)) {
                    for (CanMove reply : listBlack) {
                        if (reply.endi == GPx && reply.endj == GPy - 1) {
                            t = false;
                            break;
                        }
                    }
                } else t = false;
                if (t) continue;

                if (Rule.movePieceMove(grid,ChessGridElement.RED_GENERAL, GPx, GPy, GPx, GPy + 1)) {
                    for (CanMove reply : listBlack) {
                        if (reply.endi == GPx + 1 && reply.endj == GPy + 1) {
                            t = false;
                            break;
                        }
                    }
                } else t = false;
                if (t) continue;

                t = false;
                if (click.entry == ChessGridElement.BLACK_VEHICLE) {
                    if (click.starti == click.endi) {
                        for (CanMove reply : listRed) {
                            if (reply.endj > Math.min(click.startj, click.endj) && reply.endj < Math.max(click.startj, click.endj) && reply.endi == click.starti) {
                                t = true;
                            }
                        }
                    } else {
                        for (CanMove reply : listRed) {
                            if (reply.endi > Math.min(click.starti, click.endi) && reply.endi < Math.max(click.starti, click.endi) && reply.endj == click.startj) {
                                t = true;
                            }
                        }
                    }
                }
                if (t) continue;

                t = false;
                if (click.entry == ChessGridElement.BLACK_SOLDIER) {
                    if (click.starti == click.endi) {
                        for (CanMove reply : listRed) {
                            if (reply.endj > Math.min(click.startj, click.endj) && reply.endj < Math.max(click.startj, click.endj) && reply.endi == click.starti) {
                                t = true;
                            }
                        }
                    } else {
                        for (CanMove reply : listRed) {
                            if (reply.endi > Math.min(click.starti, click.endi) && reply.endi < Math.max(click.starti, click.endi) && reply.endj == click.startj) {
                                t = true;
                            }
                        }
                    }
                }
                if (t) continue;

                t = false;
                if (click.entry == ChessGridElement.BLACK_CANNON) {
                    //撤炮台
                    if (click.starti == click.endi) {
                        for (CanMove reply : listRed) {
                            if ((reply.starti == click.starti
                                    && reply.startj > Math.min(click.startj, click.endj)
                                    && reply.startj < Math.max(click.startj, click.endj)
                                    && (reply.endi != reply.starti || reply.endj < Math.min(click.startj, click.endj) || reply.endj > Math.max(click.startj, click.endj)))
                                    || (reply.endi == click.starti
                                    && reply.endj > Math.min(click.startj, click.endj)
                                    && reply.endj < Math.max(click.startj, click.endj))) {
                                t = true;
                                break;
                            }
                        }
                    } else {
                        for (CanMove reply : listRed) {
                            if ((reply.startj == click.startj
                                    && reply.starti > Math.min(click.starti, click.endi)
                                    && reply.starti < Math.max(click.starti, click.endi)
                                    && (reply.endj != reply.startj || reply.endi < Math.min(click.starti, click.endi) || reply.endi > Math.max(click.starti, click.endi)))
                                    || (reply.endj == click.startj
                                    && reply.endi > Math.min(click.starti, click.endi)
                                    && reply.endi < Math.max(click.starti, click.endi))) {
                                t = true;
                                break;
                            }
                        }
                    }
                }
                if (t) continue;

                t = false;
                if (click.entry == ChessGridElement.BLACK_RIDER) {
                    int iMove = Math.abs(click.endi - click.starti);
                    int jMove = Math.abs(click.endj - click.startj);
                    if (iMove == 2 && jMove == 1) {
                        if (click.endi > click.starti) {
                            for (CanMove reply : listRed) {
                                if (click.starti + 1 == reply.endi && click.startj == reply.endj) {
                                    t = true;
                                }
                            }
                        }
                        if (click.endi < click.starti) {
                            for (CanMove reply : listRed) {
                                if (click.starti - 1 == reply.endi && click.startj == reply.endj) {
                                    t = true;
                                }
                            }
                        }
                    } else if (iMove == 1 && jMove == 2) {
                        if (click.endj > click.startj) {
                            for (CanMove reply : listRed) {
                                if (click.starti == reply.endi && click.startj + 1 == reply.endj) {
                                    t = true;
                                }
                            }
                        }
                        if (click.endj < click.startj) {
                            for (CanMove reply : listRed) {
                                if (click.starti == reply.endi && click.startj - 1 == reply.endj) {
                                    t = true;
                                }
                            }
                        }
                    }
                }
                if (t) continue;
                return PlayerSide.BLACK;
            }
        }

        for (CanMove click : listRed) {
            if (grid[click.endi][click.endj] == ChessGridElement.BLACK_GENERAL) {
                int GPx = click.endi;             //将的位置x
                int GPy = click.endj;             //将的位置y
                boolean Canreply = false;

                for (CanMove reply : listBlack) {
                    if (reply.endi == click.starti && reply.endj == click.startj) {
                        Canreply = true;
                        break;
                    }
                }
                if (Canreply) continue;

                boolean t = true;
                if (Rule.movePieceMove(grid,ChessGridElement.BLACK_GENERAL, GPx, GPy, GPx - 1, GPy)) {
                    for (CanMove reply : listRed) {
                        if (reply.endi == GPx - 1 && reply.endj == GPy) {
                            t = false;
                            break;
                        }
                    }
                } else t = false;
                if (t) continue;

                t = true;
                if (Rule.movePieceMove(grid,ChessGridElement.BLACK_GENERAL, GPx, GPy, GPx + 1, GPy)) {
                    for (CanMove reply : listRed) {
                        if (reply.endi == GPx + 1 && reply.endj == GPy) {
                            t = false;
                            break;
                        }
                    }
                } else t = false;
                if (t) continue;

                if (Rule.movePieceMove(grid,ChessGridElement.BLACK_GENERAL, GPx, GPy, GPx, GPy - 1)) {
                    for (CanMove reply : listRed) {
                        if (reply.endi == GPx && reply.endj == GPy - 1) {
                            t = false;
                            break;
                        }
                    }
                } else t = false;
                if (t) continue;

                if (Rule.movePieceMove(grid,ChessGridElement.BLACK_GENERAL, GPx, GPy, GPx, GPy + 1)) {
                    for (CanMove reply : listRed) {
                        if (reply.endi == GPx + 1 && reply.endj == GPy + 1) {
                            t = false;
                            break;
                        }
                    }
                } else t = false;
                if (t) continue;

                t = false;
                if (click.entry == ChessGridElement.RED_VEHICLE) {
                    if (click.starti == click.endi) {
                        for (CanMove reply : listBlack) {
                            if (reply.endj > Math.min(click.startj, click.endj) && reply.endj < Math.max(click.startj, click.endj) && reply.endi == click.starti) {
                                t = true;
                            }
                        }
                    } else {
                        for (CanMove reply : listBlack) {
                            if (reply.endi > Math.min(click.starti, click.endi) && reply.endi < Math.max(click.starti, click.endi) && reply.endj == click.startj) {
                                t = true;
                            }
                        }
                    }
                }
                if (t) continue;

                t = false;
                if (click.entry == ChessGridElement.RED_SOLDIER) {
                    if (click.starti == click.endi) {
                        for (CanMove reply : listBlack) {
                            if (reply.endj > Math.min(click.startj, click.endj) && reply.endj < Math.max(click.startj, click.endj) && reply.endi == click.starti) {
                                t = true;
                            }
                        }
                    } else {
                        for (CanMove reply : listBlack) {
                            if (reply.endi > Math.min(click.starti, click.endi) && reply.endi < Math.max(click.starti, click.endi) && reply.endj == click.startj) {
                                t = true;
                            }
                        }
                    }
                }
                if (t) continue;

                t = false;
                if (click.entry == ChessGridElement.RED_CANNON) {
                    //撤炮台
                    if (click.starti == click.endi) {
                        for (CanMove reply : listBlack) {
                            if ((reply.starti == click.starti
                                    && reply.startj > Math.min(click.startj, click.endj)
                                    && reply.startj < Math.max(click.startj, click.endj)
                                    && (reply.endi != reply.starti || reply.endj < Math.min(click.startj, click.endj) || reply.endj > Math.max(click.startj, click.endj)))
                                    || (reply.endi == click.starti
                                    && reply.endj > Math.min(click.startj, click.endj)
                                    && reply.endj < Math.max(click.startj, click.endj))) {
                                t = true;
                                break;
                            }
                        }
                    } else {
                        for (CanMove reply : listBlack) {
                            if ((reply.startj == click.startj
                                    && reply.starti > Math.min(click.starti, click.endi)
                                    && reply.starti < Math.max(click.starti, click.endi)
                                    && (reply.endj != reply.startj || reply.endi < Math.min(click.starti, click.endi) || reply.endi > Math.max(click.starti, click.endi)))
                                    || (reply.endj == click.startj
                                    && reply.endi > Math.min(click.starti, click.endi)
                                    && reply.endi < Math.max(click.starti, click.endi))) {
                                t = true;
                                break;
                            }
                        }
                    }
                }
                if (t) continue;

                t = false;
                if (click.entry == ChessGridElement.RED_RIDER) {
                    int iMove = Math.abs(click.endi - click.starti);
                    int jMove = Math.abs(click.endj - click.startj);
                    if (iMove == 2 && jMove == 1) {
                        if (click.endi > click.starti) {
                            for (CanMove reply : listBlack) {
                                if (click.starti + 1 == reply.endi && click.startj == reply.endj) {
                                    t = true;
                                }
                            }
                        }
                        if (click.endi < click.starti) {
                            for (CanMove reply : listBlack) {
                                if (click.starti - 1 == reply.endi && click.startj == reply.endj) {
                                    t = true;
                                }
                            }
                        }
                    } else if (iMove == 1 && jMove == 2) {
                        if (click.endj > click.startj) {
                            for (CanMove reply : listBlack) {
                                if (click.starti == reply.endi && click.startj + 1 == reply.endj) {
                                    t = true;
                                }
                            }
                        }
                        if (click.endj < click.startj) {
                            for (CanMove reply : listBlack) {
                                if (click.starti == reply.endi && click.startj - 1 == reply.endj) {
                                    t = true;
                                }
                            }
                        }
                    }
                }
                if (t) continue;
                return PlayerSide.RED;
            }
        }
//        return PlayerSide.Continue;
        return null;
    }
}
