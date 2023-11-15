/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.durum;

import inra.ijpb.binary.distmap.ChamferMask2D;

/**
 *
 * @author wjrfo
 */
public class DTWMParams {

        /**
         * @return the chamferMask
         */
        public ChamferMask2D getChamferMask() {
            return chamferMask;
        }

        /**
         * @param chamferMask the chamferMask to set
         */
        public void setChamferMask(ChamferMask2D chamferMask) {
            this.chamferMask = chamferMask;
        }

        /**
         * @return the dynamic
         */
        public int getDynamic() {
            return dynamic;
        }

        /**
         * @param dynamic the dynamic to set
         */
        public void setDynamic(int dynamic) {
            this.dynamic = dynamic;
        }

        /**
         * @return the connectivity
         */
        public int getConnectivity() {
            return connectivity;
        }

        /**
         * @param connectivity the connectivity to set
         */
        public void setConnectivity(int connectivity) {
            this.connectivity = connectivity;
        }
        private ChamferMask2D chamferMask;
        private int dynamic;
        private int connectivity;


}
