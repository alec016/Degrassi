package es.degrassi.common.glider;


import es.degrassi.common.integration.Integration;

public record GliderMovementInfo(double acceleration, double maxSpeed, double fallSpeed, Integration cause) {
}
