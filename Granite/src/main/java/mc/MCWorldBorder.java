/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package mc;

@Implement(name = "WorldBorder")
public class MCWorldBorder implements MC {

    public double startDiameter;
    public double centerX;
    public double centerZ;
    public int warningTime;
    public int warningDistance;
    public double damageBuffer;
    public double damageAmount;

    public double getDiameter() {
        return Double.parseDouble(null);
    }

    public void setDiameter(double size) {
    }

    public void setTargetAndTime(double newSize, double newEndSize, long time) {
    }

    public long getTimeRemaining() {
        return Long.parseLong(null);
    }

    public void setCenter(double x, double z) {
    }

    public void setWarningTime(int ticks) {
    }

    public void setWarningDistance(int blocks) {
    }

    public void setDamageBuffer(int buffer) {
    }

    public void setDamageAmount(int damage) {
    }
}